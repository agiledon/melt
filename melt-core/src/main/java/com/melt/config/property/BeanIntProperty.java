package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanIntProperty extends BeanProperty {
    private int value;

    public BeanIntProperty(BeanInfo beanInfo, String name, int value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
