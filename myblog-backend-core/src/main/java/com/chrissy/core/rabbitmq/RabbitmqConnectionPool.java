package com.chrissy.core.rabbitmq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chrissy
 * @description Rabbitmq连接池
 * @date 2024/8/2 15:57
 */
public class RabbitmqConnectionPool {

    private static BlockingQueue<RabbitmqConnection> pool;

    /**
     * 初始化Rabbitmq连接池，容量固定
     * @param host 主机名
     * @param port 端口号
     * @param userName 用户名
     * @param password 密码
     * @param virtualhost 虚拟机号
     * @param poolSize 连接池大小
     */
    public static void initRabbitmqConnectionPool(String host, int port, String userName, String password,
                                                  String virtualhost,
                                                  Integer poolSize) {
        pool = new LinkedBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(new RabbitmqConnection(host, port, userName, password, virtualhost));
        }
    }

    /**
     * 获取Rabbitmq连接
     * @return Rabbitmq连接
     * @throws InterruptedException 中断错误
     */
    public static RabbitmqConnection getConnection() throws InterruptedException {
        return pool.take();
    }


    /**
     * 收回Rabbitmq连接
     * @param connection Rabbitmq连接
     */
    public static void takeBackConnection(RabbitmqConnection connection) {
        pool.add(connection);
    }

    /**
     * 关闭Rabbitmq池
     */
    public static void close() {
        pool.forEach(RabbitmqConnection::close);
    }
}
