package com.chrissy.core.sensitive.ibatis;

import com.chrissy.core.sensitive.SensitiveField;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author chrissy
 * @description 敏感词识别
 * @date 2024/8/6 20:26
 */
@Data
public class SensitiveObjectMeta {
    private static final String ROOT_CLASS_NAME = "java.lang.object";
    private Boolean enableSensitiveReplace;
    private String className;
    private List<SensitiveFieldMeta> sensitiveFieldMetaList;

    @Data
    public static class SensitiveFieldMeta{
        private String name;
        private String bindField;
    }

    /**
     * 根据对象构建敏感词识别堆
     * @param object 对象
     * @return 敏感词识别对象
     */
    public static Optional<SensitiveObjectMeta> buildSensitiveObjectMeta(Object object){
        if (isNull(object)){
            return Optional.empty();
        }

        Class<?> clazz = object.getClass();
        SensitiveObjectMeta sensitiveObjectMeta = new SensitiveObjectMeta();
        sensitiveObjectMeta.setClassName(clazz.getName());
        List<SensitiveFieldMeta> tempFields = new ArrayList<>();
        boolean enableReplace = parseAllSensitiveFields(clazz, tempFields);
        sensitiveObjectMeta.setSensitiveFieldMetaList(tempFields);
        sensitiveObjectMeta.setEnableSensitiveReplace(enableReplace);
        return Optional.of(sensitiveObjectMeta);
    }

    /**
     * 解析所有存在的敏感词；如果存在则开启敏感词替换
     * @param clazz 需要解析的类
     * @param sensitiveFieldMetaList 敏感词词目
     * @return 是否开启敏感词替换
     */
    private static boolean parseAllSensitiveFields(Class<?> clazz, List<SensitiveFieldMeta> sensitiveFieldMetaList) {
        Class<?> tempClazz = clazz;
        boolean hasSensitiveField = false;
        while (nonNull(tempClazz) && !tempClazz.getName().equalsIgnoreCase(ROOT_CLASS_NAME)) {
            for (Field field : tempClazz.getDeclaredFields()) {
                SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
                if (nonNull(sensitiveField)) {
                    SensitiveFieldMeta sensitiveFieldMeta = new SensitiveFieldMeta();
                    sensitiveFieldMeta.setName(field.getName());
                    sensitiveFieldMeta.setBindField(sensitiveField.bind());
                    sensitiveFieldMetaList.add(sensitiveFieldMeta);
                    hasSensitiveField = true;
                }
            }
            tempClazz = tempClazz.getSuperclass();
        }
        return hasSensitiveField;
    }
}
