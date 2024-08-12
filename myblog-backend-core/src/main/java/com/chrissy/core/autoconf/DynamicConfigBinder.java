package com.chrissy.core.autoconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.PropertySources;

/**
 * @author chrissy
 * @description 动态配置绑定
 * @date 2024/7/31 21:53
 */
public class DynamicConfigBinder {
    private final ApplicationContext context;
    private final PropertySources sources;
    private volatile Binder binder;

    /**
     * 初始化应用上下文和配置项资源
     * @param applicationContext 应用上下文
     * @param propertySource 配置项资源
     */
    public DynamicConfigBinder(ApplicationContext applicationContext, PropertySources propertySource) {
        this.context = applicationContext;
        this.sources = propertySource;
    }

    /**
     * 绑定含有ConfigurationProperties注解的可绑定对象
     * @param bindable 可绑定对象
     * @param <T> 泛型
     */
    public <T> void bind(Bindable<T> bindable) {
        ConfigurationProperties propertiesAno = bindable.getAnnotation(ConfigurationProperties.class);
        if (propertiesAno != null) {
            BindHandler bindHandler = getBindHandler(propertiesAno);
            bind(propertiesAno.prefix(), bindable, bindHandler);
        }
    }

    /**
     * 绑定特定的prefix，Bindable和BindHandler
     * @param prefix 前缀
     * @param bindable 可绑定对象
     * @param bindHandler 绑定异常处理器
     * @param <T> 泛型
     */
    public <T> void bind(String prefix, Bindable<T> bindable, BindHandler bindHandler) {
        getBinder().bind(prefix, bindable, bindHandler);
    }

    /**
     * 获取BindHandler<P>
     * 忽略顶级转换器，绑定错误，未绑定错误
     * @param annotation 需要绑定的注解
     * @return BindHandler
     */
    private BindHandler getBindHandler(ConfigurationProperties annotation) {
        BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();
        if (annotation.ignoreInvalidFields()) {
            handler = new IgnoreErrorsBindHandler(handler);
        }
        if (!annotation.ignoreUnknownFields()) {
            UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
            handler = new NoUnboundElementsBindHandler(handler, filter);
        }
        return handler;
    }

    /**
     * 根据上下文配置Binder，其中加入了<p>
     *     ConfigurationPropertySources,<p>
     *     PropertySourcesPlaceholdersResolver,<p>
     *     DefaultConversionService,<p>
     *     Consumer PropertyEditorRegistry
     * @return binder
     */
    private Binder getBinder() {
        if (this.binder == null) {
            synchronized (this) {
                if (this.binder == null) {
                    this.binder = new Binder(
                            ConfigurationPropertySources.from(this.sources),
                            new PropertySourcesPlaceholdersResolver(this.sources),
                            new DefaultConversionService(),
                            this.context instanceof ConfigurableApplicationContext ?
                                    ((ConfigurableApplicationContext) this.context).getBeanFactory()::copyRegisteredEditorsTo
                                    :
                                    null
                    );
                }
            }
        }
        return this.binder;
    }
}
