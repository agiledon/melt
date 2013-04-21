package com.melt.bean.autowired;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

import java.lang.reflect.Field;

public class AutoWiredByType extends AbstractAutoWiredBy {
    @Override
    protected Object getValue(Container parentContainer, InitializedBeans initializedBeans, Field field) {
        Object resolvedBean = initializedBeans.getBean(field.getType());
        if (resolvedBean == null && parentContainer != null) {
            resolvedBean = parentContainer.resolve(field.getType());
        }
        return resolvedBean;
    }
}
