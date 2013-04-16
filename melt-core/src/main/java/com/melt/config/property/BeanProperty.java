package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;
import com.melt.exceptions.InitBeanException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class BeanProperty {
    private String name;
    private BeanInfo beanInfo;

    public BeanProperty(BeanInfo beanInfo, String name) {
        this.name = name;
        this.beanInfo = beanInfo;
    }

    public void injectPropertyValue(BeansContainer beansContainer) {
        String beanName = beanInfo.getName();
        Object bean = beansContainer.get(beanName);
        try {
                getMethod(beansContainer).invoke(bean, new Object[]{getValue(beansContainer)});
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (InvocationTargetException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (Throwable e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        }
    }

    protected  Method getMethod(BeansContainer beansContainer) {
        Class clazz = beansContainer.getClazz(beanInfo.getName());
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && methodName.substring(3).equalsIgnoreCase(name)) {
                return method;
            }
        }
        throw new InitBeanException(String.format("The property '%s' of bean '%s' has not set method", name, beanInfo.getName()));
    }

    protected abstract Object getValue(BeansContainer beansContainer);

    public String getName() {
        return name;
    }

    protected BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
