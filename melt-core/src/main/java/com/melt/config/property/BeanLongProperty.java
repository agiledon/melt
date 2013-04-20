package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanLongProperty extends BeanProperty {
    private long value;

    public BeanLongProperty(BeanInfo beanInfo, String name, long value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
