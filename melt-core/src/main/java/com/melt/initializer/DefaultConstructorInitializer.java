package com.melt.initializer;

import com.melt.config.BeanInfo;
import com.melt.exceptions.InitBeanException;

public class DefaultConstructorInitializer implements Initializer {
    @Override
    public Object initialize(BeanInfo beanConfig) {
        if (beanConfig.isDefaultConstructorBean()) {
            return createBean(beanConfig.getClassName());
        }
        throw new InitBeanException("Can't initialize a bean with constructor configuration");
    }

    private Object createBean(String className) {
        try {
            Class clazz = getClazz(className);
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
