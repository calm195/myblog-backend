package com.chrissy.core.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author chrissy
 * @description 自定义切面AsyncExecute的后台处理机
 * @date 2024/7/24 23:18
 */

@Slf4j
@Aspect
@Component
public class AsyncExecuteAspect implements ApplicationContextAware {
    private ExpressionParser parser;
    private ApplicationContext applicationContext;

    /**
     * 超时执行兜底策略
     * @param joinPoint 切入点
     * @param asyncExecute 注解信息
     * @return 超时处理后结果
     */
    private Object defaultResponseWhenTimeOut(ProceedingJoinPoint joinPoint, AsyncExecute asyncExecute) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(this.applicationContext));

        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        log.info("{} 执行超时，执行默认超时兜底策略", methodSignature.getMethod().getName());
        return parser.parseExpression(asyncExecute.timeOutResponse()).getValue(context);
    }

    /**
     * 注解处理逻辑
     * @param joinPoint 实际的处理逻辑
     * @param asyncExecute 注解信息
     * @return 处理后结果
     * @throws Throwable 抛出RuntimeException错误
     */
    @Around("@annotation(asyncExecute)")
    public Object handle(ProceedingJoinPoint joinPoint, AsyncExecute asyncExecute) throws Throwable{
        if (!asyncExecute.turnOnAsync()){
            return joinPoint.proceed();
        }

        try {
            return AsyncUtil.callWithTimeLimit(asyncExecute.timeOut(), asyncExecute.timeOutUnit(), () -> {
               try {
                   return joinPoint.proceed();
               } catch (Throwable e) {
                   throw new RuntimeException(e);
               }
            });
        } catch (ExecutionException | InterruptedException | TimeoutException ex){
            if (StringUtils.isNoneBlank(asyncExecute.timeOutResponse())) {
                return defaultResponseWhenTimeOut(joinPoint, asyncExecute);
            } else {
                throw ex;
            }
        } catch (Exception e){
            throw e;
        }
    }

    /**
     * 设置应用上下文信息和初始化SpEL解释器
     * @param applicationContext 应用上下文信息
     * @throws BeansException Bean错误
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.parser = new SpelExpressionParser();
        this.applicationContext = applicationContext;
    }
}
