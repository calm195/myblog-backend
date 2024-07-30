package com.chrissy.core.util;

import javax.net.ServerSocketFactory;
import java.net.ServerSocket;
import java.util.Random;

/**
 * @author chrissy
 * @description Socket工具类
 * @date 2024/7/29 22:03
 */
public class SocketUtil {
    private static final Random RANDOM = new Random();

    /**
     * 在给定范围内找一个可用的TCP端口，如果默认端口可以用就返回默认端口
     * @param minPort 最小端口号
     * @param maxPort 最大端口号
     * @param defaultPort 默认端口号
     * @return 可用端口号
     */
    public static int findAvailableTcpPort(int minPort, int maxPort, int defaultPort) {
        if (isPortAvailable(defaultPort)) {
            return defaultPort;
        }

        if (maxPort <= minPort) {
            throw new IllegalArgumentException("maxPort should bigger than miPort!");
        }
        int portRange = maxPort - minPort;
        int searchCounter = 0;

        while (searchCounter <= portRange) {
            int candidatePort = generateRandomPort(minPort, maxPort);
            ++searchCounter;
            if (isPortAvailable(candidatePort)) {
                return candidatePort;
            }
        }

        throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts",
                                                    SocketUtil.class.getName(), minPort, maxPort, searchCounter));
    }

    /**
     * 判断端口是否可用
     * @param port 端口号
     * @return 是否可用
     */
    private static boolean isPortAvailable(int port){
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1);
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 生成给定范围内一个随机端口
     * @param minPort 最小端口号
     * @param maxPort 最大端口号
     * @return 随机端口号
     */
    private static int generateRandomPort(int minPort, int maxPort) {
        return minPort + RANDOM.nextInt(maxPort - minPort + 1);
    }
}
