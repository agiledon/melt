package com.melt.core;

import com.melt.config.BeanInfo;
import com.melt.config.Configuration;

import java.util.List;

public class Context {

    private BeansContainer container;

    public <T> T resolve(Class targetClass) {
        return container.resolve(targetClass);
    }

    public Object resolve(String beanName) {
        return container.resolve(beanName);
    }

    public void setContainer(BeansContainer container) {
        this.container = container;
    }
}
