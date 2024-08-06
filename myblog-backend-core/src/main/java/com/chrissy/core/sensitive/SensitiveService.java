package com.chrissy.core.sensitive;

import com.chrissy.core.autoconf.DynamicConfigContainer;
import com.chrissy.core.cache.RedisClient;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllowSystem;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author chrissy
 * @description 敏感词服务，增删改查等等
 * @date 2024/8/6 21:32
 */
@Slf4j
@Service
public class SensitiveService {
    private static final String SENSITIVE_WORD_CNT_PREFIX = "sensitive_word";
    private volatile SensitiveWordBs sensitiveWordBs;
    @Autowired
    private SensitiveProperty sensitiveConfig;
    @Autowired
    private DynamicConfigContainer dynamicConfigContainer;

    /**
     * 敏感词配置信息变化触发敏感词缓存更新
     */
    @PostConstruct
    public void refresh() {
        dynamicConfigContainer.registerRefreshCallback(sensitiveConfig, this::refresh);
        IWordDeny deny = () -> {
            List<String> sub = WordDenySystem.getInstance().deny();
            sub.addAll(sensitiveConfig.getDeny());
            return sub;
        };

        IWordAllow allow = () -> {
            List<String> sub = WordAllowSystem.getInstance().allow();
            sub.addAll(sensitiveConfig.getAllow());
            return sub;
        };
        sensitiveWordBs = SensitiveWordBs.newInstance()
                .wordDeny(deny)
                .wordAllow(allow)
                .init();
        log.info("敏感词初始化完成!");
    }

    /**
     * 判断是否包含敏感词，如果包含则写入缓存并计数
     * @param txt 需要校验的文本
     * @return 返回命中的敏感词
     */
    public List<String> contains(String txt) {
        if (!BooleanUtils.isTrue(sensitiveConfig.getEnable())) {
            return Collections.emptyList();
        }

        List<String> ans = sensitiveWordBs.findAll(txt);
        if (CollectionUtils.isEmpty(ans)) {
            return ans;
        }

        RedisClient.PipelineAction action = RedisClient.getPipelineAction();
        ans.forEach(key -> action.add(SENSITIVE_WORD_CNT_PREFIX, key, (connection, k, v) -> connection.hIncrBy(k, v, 1)));
        action.execute();
        log.info("敏感词增加计数");
        return ans;
    }


    /**
     * 返回已命中的敏感词以及命中次数
     * @return key: 敏感词， value：计数
     */
    public Map<String, Integer> getHitSensitiveWords() {
        return RedisClient.hGetAll(SENSITIVE_WORD_CNT_PREFIX, Integer.class);
    }

    /**
     * 移除敏感词
     * @param word 敏感词
     */
    public void removeSensitiveWord(String word) {
        RedisClient.hDel(SENSITIVE_WORD_CNT_PREFIX, word);
        log.info("移除敏感词：{}", word);
    }

    /**
     * 敏感词替换，将敏感词替换为*
     * @param txt 敏感词
     * @return 替换词
     */
    public String replace(String txt) {
        if (BooleanUtils.isTrue(sensitiveConfig.getEnable())) {
            return sensitiveWordBs.replace(txt);
        }
        log.info("替换敏感词：{}", txt);
        return txt;
    }

    /**
     * 查询文本中所有命中的敏感词
     * @param txt 校验文本
     * @return 命中的敏感词
     */
    public List<String> findAll(String txt) {
        return sensitiveWordBs.findAll(txt);
    }
}
