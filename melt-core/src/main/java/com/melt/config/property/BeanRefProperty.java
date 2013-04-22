package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public class BeanRefProperty extends BeanProperty {
    private String ref;

    public BeanRefProperty(BeanInfo beanInfo, String name, String ref) {
        super(beanInfo, name);
        this.ref = ref;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans, Container parentContainer) {
        Object bean = initializedBeans.getBean(ref);
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(ref);
        }
        return bean;
    }
}
