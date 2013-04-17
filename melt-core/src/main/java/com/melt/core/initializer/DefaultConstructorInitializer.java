package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.exceptions.InitBeanException;

import static com.google.common.collect.Lists.newArrayList;

public class DefaultConstructorInitializer implements Initializer {
    @Override
    public InitializedBean initialize(BeanInfo beanInfo) {
        if (beanInfo.isNotDefaultConstructorBean()) {
            Class clazz = beanInfo.getClazz();
            Object bean = createBean(clazz);
            return new InitializedBean(bean, newArrayList(clazz.getInterfaces()));
        }
        throw new InitBeanException("Can't initialize a bean with constructor configuration");
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
