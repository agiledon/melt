package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BeanProperty {
    private String name;
    private BeanInfo beanInfo;
    private Logger logger = LoggerFactory.getLogger(BeanProperty.class);

    public BeanProperty(BeanInfo beanInfo, String name) {
        this.name = name;
        this.beanInfo = beanInfo;
    }

    public void injectPropertyValue(InitializedBeans initializedBeans, Container parentContainer) {
        String beanName = beanInfo.getName();
        Object targetBean = initializedBeans.getBean(beanName);
        String message = String.format("Can't initialize bean: %s", beanName);
        try {
            getMethod().invoke(targetBean, new Object[]{getValue(initializedBeans, parentContainer)});
        } catch (IllegalAccessException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        } catch (InvocationTargetException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        } catch (Throwable e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        }
    }

    protected Method getMethod() {
        Class currentClazz = beanInfo.getClazz();
        for (Class clazz = currentClazz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                String methodName = method.getName();
                if (methodName.startsWith("set") && methodName.substring(3).equalsIgnoreCase(name)) {
                    return method;
                }
            }
        }
        String message = String.format("The property '%s' of bean '%s' has not set method", name, beanInfo.getName());
        logger.error(message);
        throw new InitBeanException(message);
    }

    protected abstract Object getValue(InitializedBeans initializedBeans, Container parentContainer);

    public String getName() {
        return name;
    }

}
