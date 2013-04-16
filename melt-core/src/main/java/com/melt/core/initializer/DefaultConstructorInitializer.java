package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.exceptions.InitBeanException;

import static com.google.common.collect.Lists.newArrayList;

public class DefaultConstructorInitializer implements Initializer {
    @Override
    public InitializedBean initialize(BeanInfo beanConfig) {
        if (beanConfig.isNotDefaultConstructorBean()) {
            Class clazz = getClazz(beanConfig.getClassName());
            Object bean = createBean(beanConfig.getClassName(), clazz);
            return new InitializedBean(bean, clazz, newArrayList(clazz.getInterfaces()));
        }
        throw new InitBeanException("Can't initialize a bean with constructor configuration");
    }

    private Object createBean(String className, Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        }
    }

    private Class getClazz(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InitBeanException(String.format("Class '%s' not found in the classpath", className), e);
        }
    }
}
