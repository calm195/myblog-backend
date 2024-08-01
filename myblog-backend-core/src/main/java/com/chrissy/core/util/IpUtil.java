package com.chrissy.core.util;

import com.chrissy.core.region.IpRegionInfo;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * @author chrissy
 * @description IP工具类
 * @date 2024/7/29 11:25
 */

@Slf4j
public class IpUtil {
    private static final String UNKNOWN = "unKnown";
    private static String LOCAL_IP = null;
    private static final String IP_DB_PATH = "data/ip2region.xdb";
    private static String tmpPath = null;
    private static volatile byte[] vIndex = null;

    public static final String DEFAULT_IP_4 = "127.0.0.1";
    public static final String DEFAULT_IP_6 = "0:0:0:0:0:0:0:1";
    public static final String DEFAULT_HOSTNAME = "localhost";

    /**
     * 获取本地IP4地址，如果网络接口没有地址，那么返回127.0.0.1
     * @return ip地址 string
     * @throws SocketException 套接字通讯错误
     */
    public static String getLocalIp4Address() throws SocketException {
        if (LOCAL_IP != null){
            return LOCAL_IP;
        }

        final List<Inet4Address> inet4AddressList = getLocalIp4AddressFromNetworkInterface();
        if (inet4AddressList.size() != 1){
            final Optional<Inet4Address> ip = getIpBySocket();
            LOCAL_IP = ip.map(Inet4Address::getHostAddress).orElseGet(() -> inet4AddressList.isEmpty() ? DEFAULT_IP_4 : inet4AddressList.get(0).getHostAddress());
            return LOCAL_IP;
        }
        LOCAL_IP = inet4AddressList.get(0).getHostAddress();
        return LOCAL_IP;
    }

    /**
     * 获取请求来源的ip地址，可以查到多次反向代理后的真实IP地址
     * @param request http请求
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        try {
            String xIp = request.getHeader("X-Real-IP");
            String xFor = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = xFor.indexOf(",");
                if (index != -1) {
                    return xFor.substring(0, index);
                } else {
                    return xFor;
                }
            }
            xFor = xIp;
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                return xFor;
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getRemoteAddr();
            }

            if (DEFAULT_HOSTNAME.equalsIgnoreCase(xFor) || DEFAULT_IP_4.equalsIgnoreCase(xFor) || DEFAULT_IP_6.equalsIgnoreCase(xFor)) {
                return getLocalIp4Address();
            }
            return xFor;
        } catch (Exception e) {
            log.error("get remote ip error!", e);
            return "x.0.0.1";
        }
    }

    /**
     * 根据IP地址获取位置信息
     * @param ip IP地址
     * @return IP定位信息
     */
    public static IpRegionInfo getLocationByIp(String ip) {
        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        initVIndex();
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithVectorIndex(tmpPath, vIndex);
            return new IpRegionInfo(searcher.search(ip));
        } catch (Exception e) {
            log.error("failed to create vectorIndex cached searcher with {}: {}\n", IP_DB_PATH, e.toString());
            return new IpRegionInfo("");
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    log.error("failed to close file:{}\n", IP_DB_PATH, e);
                }
            }
        }
    }

    /**
     * 初始化vIndex，为ip查询所属地做准备，vIndex为并行量，支持并发操作。
     */
    private static void initVIndex() {
        if (vIndex == null) {
            synchronized (IpUtil.class) {
                if (vIndex == null) {
                    try {
                        String file = IpUtil.class.getClassLoader().getResource(IP_DB_PATH).getFile();
                        if (file.contains(".jar!")) {
                            // RandomAccessFile 无法加载jar包内的文件，因此我们将资源拷贝到临时目录下
                            FileWriteUtil.FileInfo tmpFile = new FileWriteUtil.FileInfo("/tmp/data", "ip2region", "xdb");
                            tmpPath = tmpFile.getAbsFile();
                            if (!new File(tmpPath).exists()) {
                                // fixme 如果已经存在，则无需继续拷贝，因此当ip库变更之后，需要手动去删除 临时目录下生成的文件，避免出现更新不生效；更好的方式则是比较两个文件的差异性；当不同时，也需要拷贝过去
                                FileWriteUtil.saveFileByStream(IpUtil.class.getClassLoader().getResourceAsStream(IP_DB_PATH), tmpFile);
                            }
                        } else {
                            tmpPath = file;
                        }
                        vIndex = Searcher.loadVectorIndexFromFile(tmpPath);
                    } catch (Exception e) {
                        log.error("failed to load vector index from ip database {}\n", IP_DB_PATH, e);
                    }
                }
            }
        }
    }

    /**
     * 根据socket对8.8.8.8:10002通信获取ip4地址
     * @return Inet4Address对象或者Optional空对象
     * @throws SocketException socket通信错误
     */
    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException networkInterfaces) {
            throw new RuntimeException(networkInterfaces);
        }
        return Optional.empty();
    }

    /**
     * 查询本机的所有IP4地址，不包括回环、虚拟、非活动、点对点地址
     * @return IP4地址
     * @throws SocketException socket通信错误
     */
    private static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(1);

        // 所有网络接口信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (ObjectUtils.isEmpty(networkInterfaces)) {
            return addresses;
        }

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            //滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
            if (!isValidInterface(networkInterface)) {
                continue;
            }

            // 所有网络接口的IP地址信息
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                // 判断是否是IPv4，并且内网地址并过滤回环地址.
                if (isValidAddress(inetAddress)) {
                    addresses.add((Inet4Address) inetAddress);
                }
            }
        }
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     * @param address ip地址
     * @return 如果满足则true，否则false
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }
}
