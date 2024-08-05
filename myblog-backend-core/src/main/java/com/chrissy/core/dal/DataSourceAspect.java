package com.chrissy.core.dal;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author chrissy
 * @description DataSource切面，动态配置数据源
 * @date 2024/8/5 13:57
 */
@Aspect
public class DataSourceAspect {
    /**
     * 切面点，被{@code @DataSourceAno}注解的方法以及类内方法
     */
    @Pointcut("@annotation(com.chrissy.core.dal.DataSourceAno) || @within(com.chrissy.core.dal.DataSourceAno)")
    public void pointcut(){
    }

    /**
     * 环绕，将设定的数据源注入上下文并执行方法，返回方法返回值
     * @param joinPoint 切面
     * @return 方法返回值
     * @throws Throwable 可抛出的所有
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        DataSourceAno dataSourceAno = getDataSourceAno(joinPoint);
        try {
            if (dataSourceAno != null && (StringUtils.isNotBlank(dataSourceAno.value()) || dataSourceAno.preset() != null)) {
                DataSourceContextHolder.set(StringUtils.isNoneBlank(dataSourceAno.value()) ? dataSourceAno.value() : dataSourceAno.preset().name());
            }
            return joinPoint.proceed();
        } finally {
            if (dataSourceAno != null){
                DataSourceContextHolder.reset();
            }
        }
    }

    /**
     * 获取切面内的DataSourceAno注解
     * @param joinPoint 切面
     * @return DataSourceAno注解
     */
    private DataSourceAno getDataSourceAno(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DataSourceAno dataSourceAno = method.getAnnotation(DataSourceAno.class);
        if (dataSourceAno == null){
            dataSourceAno = (DataSourceAno) joinPoint.getSignature().getDeclaringType().getAnnotation(DataSourceAno.class);
        }
        return dataSourceAno;
    }
}
