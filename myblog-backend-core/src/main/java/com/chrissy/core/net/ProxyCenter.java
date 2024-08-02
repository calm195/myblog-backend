package com.chrissy.core.net;

import com.chrissy.core.config.ProxyProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chrissy
 * @description 代理中心 - 管理代理，加载删除
 * @date 2024/8/1 14:51
 */
public class ProxyCenter {
    /**
     * 记录每个source使用的proxy索引
     */
    private static final Cache<String, Integer> HOST_PROXY_INDEX = Caffeine.newBuilder().maximumSize(16).build();
    private static List<ProxyProperties.ProxyInfo> PROXIES = new ArrayList<>();

    /**
     * 初始化代理池
     * @param proxyInfos 代理信息
     */
    public static void initProxyPool(List<ProxyProperties.ProxyInfo> proxyInfos) {
        PROXIES = proxyInfos;
    }

    /**
     * 获取代理
     * @param host 主机
     * @return 代理信息
     */
    static ProxyProperties.ProxyInfo getProxy(String host) {
        Integer index = HOST_PROXY_INDEX.getIfPresent(host);
        if (index == null) {
            index = -1;
        }

        ++index;
        if (index >= PROXIES.size()) {
            index = 0;
        }
        HOST_PROXY_INDEX.put(host, index);
        return PROXIES.get(index);
    }

    /**
     * 加载代理
     * @param host 主机名
     * @return Proxy代理对象
     */
    public static Proxy loadProxy(String host) {
        ProxyProperties.ProxyInfo proxyType = getProxy(host);
        if (proxyType == null) {
            return null;
        }
        return new Proxy(proxyType.getType(), new InetSocketAddress(proxyType.getIp(), proxyType.getPort()));
    }
}
