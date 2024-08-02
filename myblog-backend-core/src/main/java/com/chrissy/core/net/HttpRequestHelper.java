package com.chrissy.core.net;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author chrissy
 * @description http请求工具类
 * @date 2024/8/1 17:02
 */
@Slf4j
public class HttpRequestHelper {
    private static final LoadingCache<String, RestTemplate> restTemplateLoadingCache;

    static {
        restTemplateLoadingCache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(key -> buildRestTemplate());
    }

    /**
     * 定时任务：每天零点清空缓存
     */
    @Scheduled(cron="0 0 0 * * ?")
    public static void cleanRestTemplate(){
        restTemplateLoadingCache.cleanUp();
    }


    /**
     * 文件上传
     * @param url       上传url
     * @param paramName 参数名
     * @param fileName  上传的文件名
     * @param bytes     上传文件流
     * @return 上传请求体
     */
    public static String upload(String url, String paramName, String fileName, byte[] bytes) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //设置请求体，注意是LinkedMultiValueMap
        ByteArrayResource fileSystemResource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        // post的文件
        form.add(paramName, fileSystemResource);

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        String threadName = Thread.currentThread().getName();
        RestTemplate restTemplate = restTemplateLoadingCache.get(threadName);
        if (restTemplate == null) {
            return null;
        }
        HttpEntity<String> res = restTemplate.postForEntity(url, files, String.class);
        return res.getBody();
    }

    /**
     * 携带代理处理请求体；如果代理无效则转为无代理
     * @param url       上传url
     * @param method     请求方法
     * @param params      请求参数列表
     * @param headers      请求头
     * @param responseClass   返回类
     * @param <R>   泛型
     * @return  返回类
     */
    public static <R> R fetchContentWithProxy(String url, HttpMethod method, Map<String, String> params,
                                              HttpHeaders headers, Class<R> responseClass) {
        R result = fetchContent(url, method, params, headers, responseClass, true);
        if (result == null) {
            return fetchContentWithoutProxy(url, method, params, headers, responseClass);
        }

        return result;
    }

    /**
     * 不携带代理处理请求体
     * @param url       上传url
     * @param method     请求方法
     * @param params      请求参数列表
     * @param headers      请求头
     * @param responseClass   返回类
     * @param <R>   泛型
     * @return  返回类
     */
    public static <R> R fetchContentWithoutProxy(String url, HttpMethod method, Map<String, String> params,
                                                 HttpHeaders headers, Class<R> responseClass) {
        return fetchContent(url, method, params, headers, responseClass, false);
    }

    /**
     * fetch content
     * @param url       上传url
     * @param method     请求方法
     * @param params     请求参数列表
     * @param headers      请求头
     * @param responseClass   返回类
     * @param useProxy     是否使用代理
     * @param <R>   泛型
     * @return   返回类
     */
    private static <R> R fetchContent(String url, HttpMethod method,
                                      Map<String, String> params,
                                      HttpHeaders headers,
                                      Class<R> responseClass, boolean useProxy) {
        String threadName = Thread.currentThread().getName();
        RestTemplate restTemplate = restTemplateLoadingCache.get(threadName);

        if (restTemplate == null){
            return null;
        }

        String host = "";
        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            log.error("Failed to parse url:{}", url);
        }

        if (useProxy) {
            ensureProxy(restTemplate, host);
        } else {
            ensureProxy(restTemplate, "");
        }

        return fetchContentInternal(restTemplate, url, method, params, headers, responseClass);
    }

    /**
     * ensure proxy
     * @param restTemplate    restTemplate
     * @param host         主机名
     */
    private static void ensureProxy(RestTemplate restTemplate, String host) {
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        if (StringUtils.isBlank(host)) {
            factory.setProxy(Proxy.NO_PROXY);
            return;
        }

        Optional.ofNullable(ProxyCenter.loadProxy(host)).ifPresent(factory::setProxy);
    }

    /**
     * fetch content
     * @param restTemplate  restTemplate
     * @param url       上传url
     * @param method     请求方法
     * @param params     请求参数列表
     * @param headers      请求头
     * @param responseClass   返回类
     * @param <R>   泛型
     * @return   返回类
     */
    @SuppressWarnings("unchecked")
    private static <R> R fetchContentInternal(RestTemplate restTemplate, String url, HttpMethod method,
                                              Map<String, String> params, HttpHeaders headers, Class<R> responseClass) {
        ResponseEntity<R> responseEntity;
        try {
            SslUtil.ignoreSsl();
            if (method.equals(HttpMethod.GET)) {
                HttpEntity<?> entity = new HttpEntity<>(headers);
                responseEntity = restTemplate.exchange(url, method, entity, responseClass, params);
            } else {
                MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
                args.setAll(params);
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(args, headers);
                responseEntity = restTemplate.exchange(url, method, entity, responseClass);
            }
        } catch (RestClientResponseException e) {
            String res = e.getResponseBodyAsString();
            if (String.class.isAssignableFrom(responseClass)) {
                return (R) res;
            } else if (JSONObject.class.isAssignableFrom(responseClass)) {
                return (R) JSONObject.parseObject(res);
            }
            return null;
        } catch (Exception e) {
            log.warn("Failed to fetch content, url:{}, params:{}, exception:{}", url, params, e.getMessage());
            return null;
        }

        return responseEntity.getBody();
    }

    /**
     * fetch content
     * @param url       上传url
     * @param params     请求参数列表
     * @param headers      请求头
     * @param responseClass   返回类
     * @param <R>   泛型
     * @return   返回类
     */
    public static <R> R fetchByRequestBody(String url, Map<String, Object> params, HttpHeaders headers,
                                           Class<R> responseClass) {
        ResponseEntity<R> responseEntity;
        try {
            String threadName = Thread.currentThread().getName();
            RestTemplate restTemplate = restTemplateLoadingCache.get(threadName);
            if (restTemplate == null){
                return null;
            }
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, responseClass);
        } catch (Exception e) {
            log.warn("Failed to fetch content, url:{}, body:{}, exception:{}", url, params, e.getMessage());
            return null;
        }

        return responseEntity.getBody();
    }

    /**
     * build rest template
     * @return 时延15s的RestTemplate
     */
    private static RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(15000);
        return new RestTemplate(factory);
    }
}
