package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanObjectProperty extends BeanProperty {
    private Object value;

    public BeanObjectProperty(BeanInfo beanInfo, String name, Object value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return value;
    }
}
