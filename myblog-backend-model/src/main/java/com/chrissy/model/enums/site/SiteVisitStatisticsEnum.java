package com.chrissy.model.enums.site;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 站点统计类型枚举
 * @date 2024/8/12 15:51
 */
@Getter
@AllArgsConstructor
public enum SiteVisitStatisticsEnum {
    EMPTY(0, ""),
    PV(1, "浏览量"),
    UV(2, "独立访客"),
    VV(3, "访问次数"),
    ;

    private final Integer code;
    private final String desc;

    public static SiteVisitStatisticsEnum formCode(Integer code) {
        for (SiteVisitStatisticsEnum value : SiteVisitStatisticsEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SiteVisitStatisticsEnum.EMPTY;
    }
}
