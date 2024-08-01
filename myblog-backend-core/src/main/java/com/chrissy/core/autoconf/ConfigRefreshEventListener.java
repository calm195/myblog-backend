package com.chrissy.core.autoconf;

import com.chrissy.model.event.ConfigRefreshEvent;
import com.chrissy.core.autoconf.property.SpringValueRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author chrissy
 * @description 监听配置刷新事件
 * @date 2024/7/31 11:35
 */
@Service
public class ConfigRefreshEventListener implements ApplicationListener<ConfigRefreshEvent> {
    @Autowired
    private DynamicConfigContainer dynamicConfigContainer;

    /**
     * 监听配置变更事件
     * @param event 事件
     */
    @Override
    public void onApplicationEvent(ConfigRefreshEvent event) {
        dynamicConfigContainer.reloadConfig();
        SpringValueRegistry.updateValue(event.getKey());
    }
}
