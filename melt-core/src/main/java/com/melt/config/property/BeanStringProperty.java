package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanStringProperty extends BeanProperty {
    private String value;

    public BeanStringProperty(String name, BeanInfo beanInfo, String value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
