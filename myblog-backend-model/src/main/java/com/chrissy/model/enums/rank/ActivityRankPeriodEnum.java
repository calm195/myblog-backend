package com.chrissy.model.enums.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 活跃度排行榜 周期
 * @date 2024/8/7 21:36
 */
@AllArgsConstructor
@Getter
public enum ActivityRankPeriodEnum {
    DAY(1, "day"),
    WEEK(2, "week"),
    MONTH(3, "month"),
    ;

    private final int type;
    private final String desc;

    public static ActivityRankPeriodEnum nameOf(String name){
        if (DAY.desc.equalsIgnoreCase(name)) {
            return DAY;
        } else if (WEEK.desc.equalsIgnoreCase(name)) {
            return WEEK;
        } else if (MONTH.desc.equalsIgnoreCase(name)) {
            return MONTH;
        }
        return null;
    }
}
