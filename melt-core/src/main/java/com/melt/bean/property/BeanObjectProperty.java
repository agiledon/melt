package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanObjectProperty extends BeanProperty {
    private Object value;

    public BeanObjectProperty(BeanInfo beanInfo, String name, Object value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
