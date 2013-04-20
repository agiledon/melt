package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;
import com.melt.exceptions.InitBeanException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BeanProperty {
    private String name;
    private BeanInfo beanInfo;
    private PropertyValue value = null;

    public BeanProperty(BeanInfo beanInfo, String name) {
        this.name = name;
        this.beanInfo = beanInfo;
    }

    protected BeanProperty(BeanInfo beanInfo, String name, PropertyValue value) {
        this(beanInfo, name);
        this.value = value;
    }

    public void injectPropertyValue(BeansContainer beansContainer) {
        String beanName = beanInfo.getName();
        Object targetBean = beansContainer.resolve(beanName);
        try {
            if (value == null) {
                value = new PropertyValue(getValue(beansContainer));
                getMethod().invoke(targetBean, new Object[]{value.getRefValue()});
            } else {
                Field field = beanInfo.getClazz().getDeclaredField(name);
                if (field != null) {
                    field.setAccessible(true);
                    if (value.isInteger()) {
                        field.setInt(targetBean, value.getInt());
                    }
                    if (value.isString()) {
                        field.set(targetBean, value.getString());
                    }
                } else {
                    throw new InitBeanException(String.format("Can't initialize bean: %s", beanName));
                }
            }
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

    protected abstract Object getValue(BeansContainer beansContainer);

    public String getName() {
        return name;
    }

    protected BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
