package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanStringProperty extends BeanProperty {
    private String value;

    public BeanStringProperty(BeanInfo beanInfo, String name, String value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
