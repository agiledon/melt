package com.melt.bean.property;

import com.melt.bean.BeanInfo;
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

    public void injectPropertyValue(InitializedBeans initializedBeans) {
        String beanName = beanInfo.getName();
        Object targetBean = initializedBeans.getBean(beanName);
        try {
            getMethod().invoke(targetBean, new Object[]{getValue(initializedBeans)});
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

    protected abstract Object getValue(InitializedBeans initializedBeans);

    public String getName() {
        return name;
    }

    protected BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
