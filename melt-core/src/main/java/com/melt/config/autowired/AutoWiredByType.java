package com.melt.config.autowired;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

import java.lang.reflect.Field;

public class AutoWiredByType extends AbstractAutoWired {

    @Override
    protected Object resolveBean(Container parentContainer, Field field) {
        return parentContainer.resolve(field.getType());
    }

    @Override
    protected Object getBean(InitializedBeans initializedBeans, Field field) {
        return initializedBeans.getBean(field.getType());
    }
}
