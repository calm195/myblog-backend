package com.chrissy.core.util;

import cn.hutool.core.lang.Assert;

/**
 * @author chrissy
 * @description 配置文件读取
 * @date 2024/7/25 9:49
 */
public class EnvUtil {
    private static volatile EnvEnum env;

    public enum EnvEnum {
        DEV("dev", false),
        TEST("test", false),
        PRE("pre", false),
        PROD("prod", true);

        private final String name;
        private final boolean isProd;

        EnvEnum(String name, boolean isProd){
            this.name = name;
            this.isProd = isProd;
        }

        public static EnvEnum getEnvByName(String name) {
            for (EnvEnum e: EnvEnum.values()) {
                if (e.name.equalsIgnoreCase(name)) {
                    return e;
                }
            }
            return null;
        }
    }

    public static boolean isProd() {
        return getEnv().isProd;
    }

    public static EnvEnum getEnv() {
        if (env == null) {
            synchronized (EnvUtil.class){
                if (env == null) {
                    env = EnvEnum.getEnvByName(SpringUtil.getConfig("env.name"));
                }
            }
        }
        Assert.isTrue(env != null, "env.name环境配置必须存在");
        return env;
    }
}
