package com.melt.bean.autowired;

import com.melt.core.InitializedBeans;

import java.lang.reflect.Field;

public class AutoWiredByType extends AbstractAutoWiredBy {
    @Override
    protected Object getValue(InitializedBeans initializedBeans, Field field) {
        return initializedBeans.getBean(field.getType());
    }
}
