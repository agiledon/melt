package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.exceptions.InitBeanException;

import static com.google.common.collect.Lists.newArrayList;

public class DefaultConstructorInitializer implements Initializer {
    @Override
    public Object initialize(BeanInfo beanInfo) {
        if (beanInfo.isDefaultConstructorBean()) {
            Class clazz = beanInfo.getClazz();
            return createBean(clazz);
        }
        return null;
    }

    private Object createBean(Class clazz) {
        String className = clazz.getName();
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        }
    }
}
