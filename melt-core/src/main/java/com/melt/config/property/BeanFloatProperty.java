package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanFloatProperty extends BeanProperty {
    private float value;

    public BeanFloatProperty(String name, BeanInfo beanInfo, float value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
