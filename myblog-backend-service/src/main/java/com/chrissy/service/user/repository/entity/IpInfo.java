package com.chrissy.service.user.repository.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * IP信息
 * @author chrissy
 * @date 2024/8/13 10:32
 */
@Data
public class IpInfo implements Serializable {
    private static final long serialVersionUID = -4612222921661930429L;

    private String firstIp;

    private String firstRegion;

    private String latestIp;

    private String latestRegion;
}
