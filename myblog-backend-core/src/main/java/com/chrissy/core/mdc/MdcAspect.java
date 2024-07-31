package com.chrissy.core.mdc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author chrissy
 * @description Mdc切面逻辑，支持SpEL
 * @date 2024/7/30 21:02
 */
@Slf4j
@Aspect
@Component
public class MdcAspect implements ApplicationContextAware {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private ApplicationContext applicationContext;

    /**
     * 定义切入点，范围为被{@code MdcDot}的注解函数以及被{@code MdcDot}的注解类内函数
     */
    @Pointcut("@annotation(MdcDot) || @within(MdcDot)")
    public void getLogAnnotation(){
    }

    /**
     * {@code @Around} 处理逻辑，处理范围为被{@code MdcDot}的注解函数以及被{@code MdcDot}的注解类内函数
     * @param joinPoint 切入点
     * @return 切入点函数运行结果
     * @throws Throwable 某些抛出
     */
    @Around("getLogAnnotation()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        boolean hasTag = addMdcCode(joinPoint);
        try {
            return joinPoint.proceed();
        } finally {
            log.info("执行耗时: {}#{} = {}ms",
                    joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    System.currentTimeMillis() - start);
            if (hasTag) {
                MdcUtil.reset();
            }
        }
    }

    /**
     * 设置当前上下文
     * @param applicationContext 上下文
     * @throws BeansException Bean解析错误
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 向多线程日志MDC上下文中加入{@code MdcDot} 注解信息
     * @param joinPoint 切入点
     * @return 是否加入成功
     */
    public boolean addMdcCode(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MdcDot mdcDot = method.getAnnotation(MdcDot.class);
        if (mdcDot == null){
            mdcDot = (MdcDot) signature.getDeclaringType().getAnnotation(MdcDot.class);
        }

        if (mdcDot != null){
            MdcUtil.add("bizCode", loadBizCode(mdcDot.bizCode(), joinPoint));
            return true;
        }
        return false;
    }

    /**
     * 注入注解{@code MdcDot}中的{@code bizCode}名称，并且根据上下文以及代理函数参数运行代理函数，并返回{@code bizCode}值
     * @param key 注解参数名 {@code bizCode}
     * @param joinPoint 切入点
     * @return {@code bizCode}值
     */
    private String loadBizCode(String key, ProceedingJoinPoint joinPoint){
        if (StringUtils.isBlank(key)) {
            return "";
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));

        String[] params = parameterNameDiscoverer.getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        if (params == null || params.length == 0){
            return "";
        }
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++){
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
