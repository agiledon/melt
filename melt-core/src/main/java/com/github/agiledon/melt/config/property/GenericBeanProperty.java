package com.github.agiledon.melt.config.property;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;

public class GenericBeanProperty<T> extends BeanProperty {
    private T value;

    public GenericBeanProperty(BeanInfo beanInfo, String name, T value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InjectionContext injectionContext) {
        return value;
    }
}
