package com.github.agiledon.melt.config.autowired;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;
import com.github.agiledon.melt.core.Container;
import com.github.agiledon.melt.core.InitializedBeans;
import com.github.agiledon.melt.exceptions.AutoWiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class AbstractAutoWired implements AutoWired {
    private Logger logger = LoggerFactory.getLogger(AbstractAutoWired.class);
    @Override
    public void autoWired(InjectionContext injectionContext, BeanInfo beanInfo) {
        Class clazz = beanInfo.getClazz();
        Field[] fields = clazz.getDeclaredFields();
        setFieldValues(injectionContext, beanInfo, fields);
        setSuperFiledValues(clazz, injectionContext, beanInfo);
    }

    private void setSuperFiledValues(Class clazz, InjectionContext injectionContext, BeanInfo beanInfo) {
        Class superclass = clazz.getSuperclass();
        if (superclass.getName().equals(Object.class.getName())) {
            return;
        }
        Field[] declaredFields = superclass.getDeclaredFields();
        setFieldValues(injectionContext, beanInfo, declaredFields);
        setSuperFiledValues(superclass, injectionContext, beanInfo);
    }

    private void setFieldValues(InjectionContext injectionContext, BeanInfo beanInfo, Field[] fields) {
        Object bean = injectionContext.getInitializedBeans().getBean(beanInfo.getName());
        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            if (modifiers.contains("static") || modifiers.contains("final")) {
                continue;
            }
            field.setAccessible(true);
            try {
                if (isNotPrimitive(field)) {
                    field.set(bean, getValue(injectionContext, field));
                }
            } catch (Throwable e) {
                String message = String.format("AutoWired %s of %s failed!", field.getName(), beanInfo.getName());
                logger.error(message);
                throw new AutoWiredException(message);
            }
        }
    }

    protected Object getValue(InjectionContext injectionContext, Field field) {
        Object resolvedBean = getBean(injectionContext.getInitializedBeans(), field);
        if (resolvedBean == null && injectionContext.getParentContainer() != null) {
            resolvedBean = resolveBean(injectionContext.getParentContainer(), field);
        }
        return resolvedBean;
    }

    private boolean isNotPrimitive(Field field) {
        return !field.getType().isPrimitive() && !field.getType().getName().equals(String.class.getName());
    }

    protected abstract Object resolveBean(Container parentContainer, Field field);

    protected abstract Object getBean(InitializedBeans initializedBeans, Field field);
}
