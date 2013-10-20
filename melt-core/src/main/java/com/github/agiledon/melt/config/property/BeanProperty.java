package com.github.agiledon.melt.config.property;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;
import com.github.agiledon.melt.exceptions.InitBeanException;
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

    public void injectPropertyValue(InjectionContext injectionContext) {
        String beanName = beanInfo.getName();
        Object targetBean = injectionContext.getInitializedBeans().getBean(beanInfo.getClazz());
        if (targetBean == null) {
            targetBean = injectionContext.getInitializedBeans().getBean(beanName);
        }
        String message = String.format("Can't initialize bean: %s", beanName);
        try {
            if (targetBean != null) {
                getSetMethod().invoke(targetBean, getInputParameters(injectionContext));
            }
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

    private Object[] getInputParameters(InjectionContext injectionContext) {
        return new Object[]{getValue(injectionContext)};
    }

    protected Method getSetMethod() {
        Class currentClazz = beanInfo.getClazz();
        for (Class clazz = currentClazz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                String methodName = method.getName();
                if (isTargetSetMethodFound(methodName)) {
                    return method;
                }
            }
        }
        String message = String.format("The property '%s' of bean '%s' has not set method", name, beanInfo.getName());
        logger.error(message);
        throw new InitBeanException(message);
    }

    private boolean isTargetSetMethodFound(String methodName) {
        return methodName.startsWith("set") && methodName.substring(3).equalsIgnoreCase(name);
    }

    protected abstract Object getValue(InjectionContext injectionContext);

    public String getName() {
        return name;
    }

}
