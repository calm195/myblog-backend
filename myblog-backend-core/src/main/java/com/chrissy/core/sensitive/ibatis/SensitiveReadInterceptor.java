package com.chrissy.core.sensitive.ibatis;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chrissy.core.sensitive.SensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author chrissy
 * @description 敏感词拦截器，对从db中读取的数据进行敏感词替换
 * @date 2024/8/6 21:23
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
public class SensitiveReadInterceptor implements Interceptor {
    private static final String MAPPED_STATEMENT = "mappedStatement";

    @Autowired
    private SensitiveService sensitiveService;

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        final List<Object> results = (List<Object>) invocation.proceed();

        if (results.isEmpty()) {
            return results;
        }

        final ResultSetHandler statementHandler = getRealTarget(invocation.getTarget());
        final MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        final MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MAPPED_STATEMENT);

        Optional<Object> firstOpt = results.stream().filter(Objects::nonNull).findFirst();
        if (!firstOpt.isPresent()) {
            return results;
        }
        Object firstObject = firstOpt.get();
        // 找到需要进行敏感词替换的数据库实体类的成员信息
        SensitiveObjectMeta sensitiveObjectMeta = findSensitiveObjectMeta(firstObject);

        // 执行替换的敏感词替换
        replaceSensitiveResults(results, mappedStatement, sensitiveObjectMeta);
        return results;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    /**
     * 执行具体的敏感词替换
     * @param results 接受结果
     * @param mappedStatement 实际数据操作的节点
     * @param sensitiveObjectMeta 敏感词元数据
     */
    @SuppressWarnings("unchecked")
    private void replaceSensitiveResults(Collection<Object> results, MappedStatement mappedStatement, SensitiveObjectMeta sensitiveObjectMeta) {
        for (Object obj : results) {
            if (sensitiveObjectMeta.getSensitiveFieldMetaList() == null) {
                continue;
            }

            final MetaObject objMetaObject = mappedStatement.getConfiguration().newMetaObject(obj);
            sensitiveObjectMeta.getSensitiveFieldMetaList().forEach(i -> {
                Object value = objMetaObject.getValue(StringUtils.isBlank(i.getBindField()) ? i.getName() : i.getBindField());
                if (value == null) {
                    return;
                }
                if (value instanceof String) {
                    String strValue = (String) value;
                    String processVal = sensitiveService.replace(strValue);
                    objMetaObject.setValue(i.getName(), processVal);
                } else if (value instanceof Collection) {
                    Collection<Object> listValue = (Collection<Object>) value;
                    if (CollectionUtils.isNotEmpty(listValue)) {
                        Optional<?> firstValOpt = listValue.stream().filter(Objects::nonNull).findFirst();
                        if (firstValOpt.isPresent()) {
                            SensitiveObjectMeta valSensitiveObjectMeta = findSensitiveObjectMeta(firstValOpt.get());
                            if (Boolean.TRUE.equals(valSensitiveObjectMeta.getEnableSensitiveReplace())
                                    && CollectionUtils.isNotEmpty(valSensitiveObjectMeta.getSensitiveFieldMetaList())) {
                                replaceSensitiveResults(listValue, mappedStatement, valSensitiveObjectMeta);
                            }
                        }
                    }
                } else if (!ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
                    SensitiveObjectMeta valSensitiveObjectMeta = findSensitiveObjectMeta(value);
                    if (Boolean.TRUE.equals(valSensitiveObjectMeta.getEnableSensitiveReplace())
                            && CollectionUtils.isNotEmpty(valSensitiveObjectMeta.getSensitiveFieldMetaList())) {
                        replaceSensitiveResults(newArrayList(value), mappedStatement, valSensitiveObjectMeta);
                    }
                }
            });
        }
    }

    /**
     * 查询对象中携带有 @SensitiveField 的成员，即敏感词元数据
     * @param object 待查询的对象
     * @return 返回对象的敏感词元数据
     */
    private SensitiveObjectMeta findSensitiveObjectMeta(Object object) {
        SensitiveMetaCache.computeIfAbsent(object.getClass().getName(), s -> {
            Optional<SensitiveObjectMeta> sensitiveObjectMetaOpt = SensitiveObjectMeta.buildSensitiveObjectMeta(object);
            return sensitiveObjectMetaOpt.orElse(null);
        });

        return SensitiveMetaCache.get(object.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private static <T> T getRealTarget(Object target){
        if (Proxy.isProxyClass(target.getClass())){
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return getRealTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }
}
