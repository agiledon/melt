package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.config.InjectionContext;

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
