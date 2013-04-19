package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanObjectProperty extends BeanProperty {
    private Object value;

    public BeanObjectProperty(String name, BeanInfo beanInfo, Object value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
