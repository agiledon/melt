package com.melt.core;

import com.melt.config.BeanInfo;
import com.melt.config.Configuration;

import java.util.List;

public class Context {

    private BeansContainer container;
    private Context parentContext;

    public <T> T resolve(Class targetClass) {
        T bean = container.resolve(targetClass);
        if (bean == null && parentContext != null) {
            bean = parentContext.resolve(targetClass);
        }
        return bean;
    }

    public Object resolve(String beanName) {
        Object bean = container.resolve(beanName);
        if (bean == null) {
            bean = parentContext.resolve(beanName);
        }
        return bean;
    }

    public void setContainer(BeansContainer container) {
        this.container = container;
    }

    public void setParentContext(Context parentContext) {
        this.parentContext = parentContext;
    }
}
