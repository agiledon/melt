package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;
import com.melt.exceptions.AutoWiredException;

import java.lang.reflect.Field;

public class AutoWiredByName implements AutoWired {
    public void autoWired(BeansContainer beansContainer, BeanInfo beanInfo) {
        Field[] fields = beanInfo.getClazz().getDeclaredFields();
        Object bean = beansContainer.resolve(beanInfo.getName());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (isNotPrimitive(field)) {
                    field.set(bean, beansContainer.resolve(field.getName()));
                }
            } catch (Throwable e) {
                throw new AutoWiredException(String.format("AutoWired %s of % % failed!", field.getName(), beanInfo.getName()));
            }
        }
    }

    private boolean isNotPrimitive(Field field) {
        return !field.getType().isPrimitive();
    }
}
