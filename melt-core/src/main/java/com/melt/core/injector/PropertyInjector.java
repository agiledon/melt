package com.melt.core.injector;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.melt.config.BeanInfo;
import com.melt.config.BeanProperty;
import com.melt.exceptions.InitBeanException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Iterators.tryFind;
import static com.google.common.collect.Lists.newArrayList;

public class PropertyInjector implements Injector {
    @Override
    public void inject(Map<String, Object> beans, List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            if (beanInfo.isNotDefaultConstructorBean()) {
                String className = beanInfo.getClassName();
                String beanName = beanInfo.getName();
                Class clazz = getClazz(className);
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (BeanProperty beanProperty : beanInfo.getProperties()) {
                    Method setPropertyMethod = getSetPropertyMethod(declaredMethods, beanProperty);
                    setPropertyValue(beans, beanName, beanProperty, setPropertyMethod);
                }
            }
        }
    }

    private Method getSetPropertyMethod(Method[] declaredMethods, final BeanProperty beanProperty) {
        Optional<Method> optional = tryFind(newArrayList(declaredMethods).iterator(), isSetPropertyMethod(beanProperty.getName()));
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    private Predicate<Method> isSetPropertyMethod(final String propertyName) {
        return new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                String methodName = method.getName();
                return methodName.startsWith("set") && methodName.substring(3).equalsIgnoreCase(propertyName);
            }
        };
    }

    private void setPropertyValue(Map<String, Object> beans, String beanName, BeanProperty beanProperty, Method method) {
        Object bean = beans.get(beanName);
        try {
            if (beanProperty.getRef() != null) {
                method.invoke(bean, new Object[]{beans.get(beanProperty.getRef())});
            }
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (InvocationTargetException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        } catch (Throwable e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", beanName), e);
        }
    }

    private Class getClazz(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InitBeanException(String.format("%s not found in the classpath", className), e);
        }
    }
}
