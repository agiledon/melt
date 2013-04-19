package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanIntProperty extends BeanProperty {
    private int value;

    public BeanIntProperty(String name, BeanInfo beanInfo, int value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
