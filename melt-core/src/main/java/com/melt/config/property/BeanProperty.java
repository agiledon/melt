package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BeanProperty {
    private String name;
    private BeanInfo beanInfo;

    public BeanProperty(BeanInfo beanInfo, String name) {
        this.name = name;
        this.beanInfo = beanInfo;
    }

    public void injectPropertyValue(InitializedBeans initializedBeans, Container parentContainer) {
        String beanName = beanInfo.getName();
        Object targetBean = initializedBeans.getBean(beanName);
        try {
            getMethod().invoke(targetBean, new Object[]{getValue(initializedBeans, parentContainer)});
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (InvocationTargetException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (Throwable e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        }
    }

    protected Method getMethod() {
        Class clazz = beanInfo.getClazz();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && methodName.substring(3).equalsIgnoreCase(name)) {
                return method;
            }
        }
        throw new InitBeanException(String.format("The property '%s' of bean '%s' has not set method", name, beanInfo.getName()));
    }

    protected abstract Object getValue(InitializedBeans initializedBeans, Container parentContainer);

    public String getName() {
        return name;
    }

    protected BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
