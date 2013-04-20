package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanIntProperty extends BeanProperty {
    private int value;

    public BeanIntProperty(BeanInfo beanInfo, String name, int value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
