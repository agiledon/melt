package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.AutoWiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class AbstractAutoWired implements AutoWired {
    private Logger logger = LoggerFactory.getLogger(AbstractAutoWired.class);
    @Override
    public void autoWired(Container parentContainer, InitializedBeans initializedBeans, BeanInfo beanInfo) {
        Field[] fields = beanInfo.getClazz().getDeclaredFields();
        Object bean = initializedBeans.getBean(beanInfo.getName());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (isNotPrimitive(field)) {
                    field.set(bean, getValue(parentContainer, initializedBeans, field));
                }
            } catch (Throwable e) {
                String message = String.format("AutoWired %s of %s failed!", field.getName(), beanInfo.getName());
                logger.error(message);
                throw new AutoWiredException(message);
            }
        }
    }

    protected Object getValue(Container parentContainer, InitializedBeans initializedBeans, Field field) {
        Object resolvedBean = getBean(initializedBeans, field);
        if (resolvedBean == null && parentContainer != null) {
            resolvedBean = resolveBean(parentContainer, field);
        }
        return resolvedBean;
    }

    private boolean isNotPrimitive(Field field) {
        return !field.getType().isPrimitive();
    }

    protected abstract Object resolveBean(Container parentContainer, Field field);

    protected abstract Object getBean(InitializedBeans initializedBeans, Field field);
}
