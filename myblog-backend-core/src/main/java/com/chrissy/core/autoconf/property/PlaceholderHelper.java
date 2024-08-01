package com.chrissy.core.autoconf.property;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * @author chrissy
 * @description 原始bean字符串解析器
 * @date 2024/7/31 11:36
 */
public class PlaceholderHelper {
    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String SIMPLE_PLACEHOLDER_PREFIX = "{";
    private static final String PLACEHOLDER_SUFFIX = "}";
    private static final String VALUE_SEPARATOR = ":";
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";

    /**
     * Resolve placeholder property values, e.g.
     * <br />
     * <br />
     * "${somePropertyValue}" -> "the actual property value"
     */
    public Object resolvePropertyValue(ConfigurableBeanFactory beanFactory, String beanName, String placeholder) {
        // resolve string value
        String strVal = beanFactory.resolveEmbeddedValue(placeholder);

        BeanDefinition bd = (beanFactory.containsBean(beanName) ? beanFactory
                .getMergedBeanDefinition(beanName) : null);

        // resolve expressions like "#{systemProperties.myProp}"
        return evaluateBeanDefinitionString(beanFactory, strVal, bd);
    }

    /**
     * 解析Bean获取变量
     * @param beanFactory beanFactory
     * @param value 原始
     * @param beanDefinition beanDefinition
     * @return 变量
     */
    private Object evaluateBeanDefinitionString(ConfigurableBeanFactory beanFactory, String value,
                                                BeanDefinition beanDefinition) {
        if (beanFactory.getBeanExpressionResolver() == null) {
            return value;
        }
        Scope scope = (beanDefinition != null ?
                beanFactory.getRegisteredScope(beanDefinition.getScope())
                :
                null);
        return beanFactory.getBeanExpressionResolver()
                .evaluate(value, new BeanExpressionContext(beanFactory, scope));
    }

    /**
     * Extract keys from placeholder, e.g.
     * <ul>
     * <li>${some.key} => "some.key"</li>
     * <li>${some.key:${some.other.key:100}} => "some.key", "some.other.key"</li>
     * <li>${${some.key}} => "some.key"</li>
     * <li>${${some.key:other.key}} => "some.key"</li>
     * <li>${${some.key}:${another.key}} => "some.key", "another.key"</li>
     * <li>#{new java.text.SimpleDateFormat('${some.key}').parse('${another.key}')} => "some.key", "another.key"</li>
     * </ul>
     */
    public Set<String> extractPlaceholderKeys(String propertyString) {
        Set<String> placeholderKeys = new HashSet<>();

        if (!isNormalizedPlaceholder(propertyString) && !isExpressionWithPlaceholder(propertyString)) {
            return placeholderKeys;
        }

        Stack<String> stack = new Stack<>();
        stack.push(propertyString);

        while (!stack.isEmpty()) {
            String strVal = stack.pop();
            int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
            if (startIndex == -1) {
                placeholderKeys.add(strVal);
                continue;
            }
            int endIndex = findPlaceholderEndIndex(strVal, startIndex);
            if (endIndex == -1) {
                // invalid placeholder?
                continue;
            }

            String placeholderCandidate = strVal.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);

            // ${some.key:other.key}
            if (placeholderCandidate.startsWith(PLACEHOLDER_PREFIX)) {
                stack.push(placeholderCandidate);
            } else {
                // some.key:${some.other.key:100}
                int separatorIndex = placeholderCandidate.indexOf(VALUE_SEPARATOR);

                if (separatorIndex == -1) {
                    stack.push(placeholderCandidate);
                } else {
                    stack.push(placeholderCandidate.substring(0, separatorIndex));
                    String defaultValuePart =
                            normalizeToPlaceholder(placeholderCandidate.substring(separatorIndex + VALUE_SEPARATOR.length()));
                    if (!StringUtils.isEmpty(defaultValuePart)) {
                        stack.push(defaultValuePart);
                    }
                }
            }

            // has remaining part, e.g. ${a}.${b}
            if (endIndex + PLACEHOLDER_SUFFIX.length() < strVal.length() - 1) {
                String remainingPart = normalizeToPlaceholder(strVal.substring(endIndex + PLACEHOLDER_SUFFIX.length()));
                if (!StringUtils.isEmpty(remainingPart)) {
                    stack.push(remainingPart);
                }
            }
        }

        return placeholderKeys;
    }

    /**
     * 是否是${propertyString}这样的
     * @param propertyString 待判定字符串
     * @return 是否满足
     */
    private boolean isNormalizedPlaceholder(String propertyString) {
        return propertyString.startsWith(PLACEHOLDER_PREFIX) && propertyString.endsWith(PLACEHOLDER_SUFFIX);
    }

    /**
     * 是否是#{xx${xx}xx}这样的
     * @param propertyString 待判定字符串
     * @return 是否满足
     */
    private boolean isExpressionWithPlaceholder(String propertyString) {
        return propertyString.startsWith(EXPRESSION_PREFIX) && propertyString.endsWith(EXPRESSION_SUFFIX)
                && propertyString.contains(PLACEHOLDER_PREFIX);
    }

    /**
     * 从字符串中取出${xxx}这样的
     * @param strVal 目标字符串
     * @return ${xxx}
     */
    private String normalizeToPlaceholder(String strVal) {
        int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
        if (startIndex == -1) {
            return null;
        }
        int endIndex = strVal.lastIndexOf(PLACEHOLDER_SUFFIX);
        if (endIndex == -1) {
            return null;
        }

        return strVal.substring(startIndex, endIndex + PLACEHOLDER_SUFFIX.length());
    }

    /**
     * 从给定的index和buf开始，找出${xxx}这样的最后index
     * @param buf 给定的buf
     * @param startIndex 给定的index
     * @return 目标索引
     */
    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + PLACEHOLDER_PREFIX.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, PLACEHOLDER_SUFFIX)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + PLACEHOLDER_SUFFIX.length();
                } else {
                    return index;
                }
            } else if (StringUtils.substringMatch(buf, index, SIMPLE_PLACEHOLDER_PREFIX)) {
                withinNestedPlaceholder++;
                index = index + SIMPLE_PLACEHOLDER_PREFIX.length();
            } else {
                index++;
            }
        }
        return -1;
    }
}
