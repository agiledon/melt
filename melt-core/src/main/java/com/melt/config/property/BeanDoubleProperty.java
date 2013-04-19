package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanDoubleProperty extends BeanProperty {
    private double value;

    public BeanDoubleProperty(String name, BeanInfo beanInfo, double value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
