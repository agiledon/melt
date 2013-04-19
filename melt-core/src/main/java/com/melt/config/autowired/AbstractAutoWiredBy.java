package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;
import com.melt.exceptions.AutoWiredException;

import java.lang.reflect.Field;

public abstract class AbstractAutoWiredBy implements AutoWired {
    @Override
    public void autoWired(BeansContainer beansContainer, BeanInfo beanInfo) {
        Field[] fields = beanInfo.getClazz().getDeclaredFields();
        Object bean = beansContainer.resolve(beanInfo.getName());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (isNotPrimitive(field)) {
                    field.set(bean, getValue(beansContainer, field));
                }
            } catch (Throwable e) {
                throw new AutoWiredException(String.format("AutoWired %s of %s failed!", field.getName(), beanInfo.getName()));
            }
        }
    }

    protected abstract Object getValue(BeansContainer beansContainer, Field field);

    private boolean isNotPrimitive(Field field) {
        return !field.getType().isPrimitive();
    }
}
