package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanFloatProperty extends BeanProperty {
    private float value;

    public BeanFloatProperty(BeanInfo beanInfo, String name, float value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
