package com.melt.bean.property;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public class BeanLongProperty extends BeanProperty {
    private long value;

    public BeanLongProperty(BeanInfo beanInfo, String name, long value) {
        super(beanInfo, name);
        this.value = value;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans) {
        return value;
    }
}
