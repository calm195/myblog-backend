package com.chrissy.core.dal;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.chrissy.core.util.DateUtil;
import com.mysql.cj.MysqlConnection;
import com.zaxxer.hikari.pool.HikariProxyConnection;
import com.zaxxer.hikari.pool.HikariProxyPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @author chrissy
 * @description Mybatis拦截
 * @date 2024/8/5 17:17
 */
@Slf4j
public class SqlStateInterceptor implements Interceptor {
    /**
     * Mybatis拦截，输出执行情况
     * @param invocation 拦截源
     * @return 执行结果
     * @throws Throwable 抛出错误
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long time = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String sql = buildSql(statementHandler);
        Object[] args = invocation.getArgs();
        String name = "";
        if (args[0] instanceof HikariProxyPreparedStatement){
            HikariProxyConnection connection = (HikariProxyConnection) ((HikariProxyPreparedStatement) invocation.getArgs()[0]).getConnection();
            name = connection.getMetaData().getUserName();
        } else if (DruidUtil.hasDruidPkg()){
            if (args[0] instanceof DruidPooledPreparedStatement) {
                Connection connection = ((DruidPooledPreparedStatement) args[0]).getStatement().getConnection();
                if (connection instanceof MysqlConnection){
                    Properties properties = ((MysqlConnection) connection).getProperties();
                    name = properties.getProperty("user");
                }
            }
        }

        Object res;
        try {
            res = invocation.proceed();
        } catch (Throwable e){
            log.error("error sql statement: {}, exception: {}", sql, e.getMessage());
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - time;
            sql = this.removeContinueSpace(sql);
            log.info("\n\n ============= \nsql ----> {}\nuser ----> {}\ncost ----> {}\n ============= \n", sql, name, cost);
        }
        return res;
    }

    /**
     * 拼接sql
     * @param statementHandler sql语句处理器
     * @return sql语句
     */
    private String buildSql(StatementHandler statementHandler) {
        BoundSql boundSql = statementHandler.getBoundSql();
        Configuration configuration = null;
        if (statementHandler.getParameterHandler() instanceof DefaultParameterHandler) {
            DefaultParameterHandler handler = (DefaultParameterHandler) statementHandler.getParameterHandler();
            configuration = (Configuration) ReflectionUtils.getFieldVal(handler, "configuration", false);
        } else if (statementHandler.getParameterHandler() instanceof MybatisParameterHandler) {
            MybatisParameterHandler paramHandler = (MybatisParameterHandler) statementHandler.getParameterHandler();
            configuration = ((MappedStatement) ReflectionUtils.getFieldVal(paramHandler, "mappedStatement", false)).getConfiguration();
        }

        if (configuration == null) {
            return boundSql.getSql();
        }

        return getSql(boundSql, configuration);
    }

    /**
     * 生成SQL语句
     * @param boundSql 自动生成的sql语句，需要进一步处理
     * @param configuration 配置信息
     * @return Sql语句
     */
    private String getSql(BoundSql boundSql, Configuration configuration) {
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (CollectionUtils.isEmpty(parameterMappings) || parameterObject == null) {
            return sql;
        }

        MetaObject mo = configuration.newMetaObject(boundSql.getParameterObject());
        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() == ParameterMode.OUT) {
                continue;
            }

            //参数值
            Object value;
            //获取参数名称
            String propertyName = parameterMapping.getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                //获取参数值
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                //如果是单个值则直接赋值
                value = parameterObject;
            } else {
                value = mo.getValue(propertyName);
            }
            String param = Matcher.quoteReplacement(getParameter(value));
            sql = sql.replaceFirst("\\?", param);
        }
        sql += ";";
        return sql;
    }

    /**
     * 生成sql语句中的变量名或值
     * @param parameter 变量
     * @return 格式化后并且加上引号的字符串
     */
    private String getParameter(Object parameter) {
        if (parameter instanceof String) {
            return "'" + parameter + "'";
        } else if (parameter instanceof java.sql.Date) {
            // 日期格式化
            return "'" + DateUtil.format(DateUtil.DB_FORMAT, ((Date) parameter).getTime()) + "'";
        } else if (parameter instanceof java.util.Date) {
            // 日期格式化
            return "'" + DateUtil.format(DateUtil.DB_FORMAT, ((java.util.Date) parameter).getTime()) + "'";
        }
        return parameter.toString();
    }

    /**
     * 去除重复的空白
     * @param str 待去除的字符串
     * @return 去除重复空白的字符串
     */
    private String removeContinueSpace(String str) {
        StringBuilder builder = new StringBuilder(str.length());
        boolean preSpace = false;
        for (int i = 0, len = str.length(); i < len; i++) {
            char ch = str.charAt(i);
            boolean isSpace = Character.isWhitespace(ch);
            if (preSpace && isSpace) {
                continue;
            }

            if (preSpace) {
                preSpace = false;
                builder.append(ch);
            } else if (isSpace) {
                preSpace = true;
                builder.append(" ");
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}
