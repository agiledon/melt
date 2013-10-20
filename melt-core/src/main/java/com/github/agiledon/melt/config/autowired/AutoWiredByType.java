package com.github.agiledon.melt.config.autowired;

import com.github.agiledon.melt.core.Container;
import com.github.agiledon.melt.core.InitializedBeans;

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
