package com.chrissy.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author chrissy
 * @description Cookie工具类
 * @date 2024/7/29 18:04
 */
public class CookieUtil {
    // 60 * 60 * 24 * 30 = 3600 * 24 * 30 = 86400 * 30 = 2592000
    private static final int COOKIE_AGE = 2_592_000;

    /**
     * 创建一个Cookie
     * @param key key
     * @param session sessionID
     * @return Cookie对象
     */
    public static Cookie newCookie(String key, String session){
        return newCookie(key, session, "/", COOKIE_AGE);
    }

    /**
     * 创建一个Cookie，并且设置生效路径和生效时间
     * @param key key
     * @param session sessionID
     * @param path 生效路径
     * @param maxAge 生效时间，单位为秒 - s
     * @return Cookie对象
     */
    public static Cookie newCookie(String key, String session, String path, int maxAge){
        Cookie cookie = new Cookie(key, session);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * 删除Cookie
     * @param key key
     * @return 失效的Cookie
     */
    public static Cookie delCookie(String key) {
        return delCookie(key, "/");
    }

    /**
     * 删除Cookie，实际上通过设定生效时间为0，使得Cookie立即失效即为删除。
     * @param key key
     * @param path 生效路径
     * @return 失效的Cookie
     */
    public static Cookie delCookie(String key, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(0);
        return cookie;
    }

    /**
     * 通过名称找到Cookie对象，getCookies()，通用方法
     * @param request http请求体
     * @param name 名称，不区分大小写
     * @return 对应的Cookie对象或者null
     */
    public static Cookie findCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        return Arrays.stream(cookies).filter(cookie -> StringUtils.equalsAnyIgnoreCase(cookie.getName(), name))
                .findFirst().orElse(null);
    }

    /**
     * 通过名称找到Cookie对象，请求头，对多次代理有效
     * @param request http请求体
     * @param name 名称，不区分大小写
     * @return 对应的Cookie对象或者null
     */
    public static String findCookieByName(ServerHttpRequest request, String name) {
        List<String> list = request.getHeaders().get("cookie");
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        for (String sub : list) {
            String[] elements = StringUtils.split(sub, ";");
            for (String element : elements) {
                String[] subs = StringUtils.split(element, "=");
                if (subs.length == 2 && StringUtils.equalsAnyIgnoreCase(subs[0].trim(), name)) {
                    return subs[1].trim();
                }
            }
        }
        return null;
    }
}
