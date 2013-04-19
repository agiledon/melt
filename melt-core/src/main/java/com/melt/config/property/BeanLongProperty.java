package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanLongProperty extends BeanProperty {
    private long value;

    public BeanLongProperty(String name, BeanInfo beanInfo, long value) {
        super(name, beanInfo);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
