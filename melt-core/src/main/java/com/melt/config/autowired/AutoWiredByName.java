package com.melt.config.autowired;

import com.melt.core.InjectionContext;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;

import java.lang.reflect.Field;

public class AutoWiredByName extends AbstractAutoWired {

    @Override
    protected Object getValue(InjectionContext injectionContext, Field field) {
        Object resolvedBean = injectionContext.getInitializedBeans().getBean(field.getName());
        if (resolvedBean == null && injectionContext.getParentContainer() != null) {
            resolvedBean = injectionContext.getParentContainer().resolve(field.getName());
        }
        return resolvedBean;
    }

    @Override
    protected Object resolveBean(Container parentContainer, Field field) {
        return parentContainer.resolve(field.getName());
    }

    @Override
    protected Object getBean(InitializedBeans initializedBeans, Field field) {
        return initializedBeans.getBean(field.getName());
    }
}
