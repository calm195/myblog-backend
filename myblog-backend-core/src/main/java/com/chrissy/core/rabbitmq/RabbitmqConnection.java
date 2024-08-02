package com.chrissy.core.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chrissy
 * @description Rabbitmq连接器
 * @date 2024/8/2 15:54
 */
@Getter
@Slf4j
public class RabbitmqConnection {
    private Connection connection;

    /**
     * 创建rabbitmq连接
     * @param host 主机名
     * @param port 端口号
     * @param userName 用户名
     * @param password 密码
     * @param virtualhost 虚拟机号
     */
    public RabbitmqConnection(String host, int port, String userName, String password, String virtualhost) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualhost);
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            log.error("RabbitMQ 创建链接失败！ {}", e.getMessage());
        }
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            log.error("RabbitMQ 关闭失败！ {}", e.getMessage());
        }
    }
}
