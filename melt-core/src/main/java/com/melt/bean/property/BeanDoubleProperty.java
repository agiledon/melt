package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanDoubleProperty extends BeanProperty {
    private double value;

    public BeanDoubleProperty(BeanInfo beanInfo, String name, double value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
