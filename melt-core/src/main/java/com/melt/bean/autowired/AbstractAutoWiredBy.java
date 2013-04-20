package com.melt.bean.autowired;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.AutoWiredException;

import java.lang.reflect.Field;

public abstract class AbstractAutoWiredBy implements AutoWired {
    @Override
    public void autoWired(InitializedBeans initializedBeans, BeanInfo beanInfo) {
        Field[] fields = beanInfo.getClazz().getDeclaredFields();
        Object bean = initializedBeans.getBean(beanInfo.getName());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (isNotPrimitive(field)) {
                    field.set(bean, getValue(initializedBeans, field));
                }
            } catch (Throwable e) {
                throw new AutoWiredException(String.format("AutoWired %s of %s failed!", field.getName(), beanInfo.getName()));
            }
        }
    }

    protected abstract Object getValue(InitializedBeans initializedBeans, Field field);

    private boolean isNotPrimitive(Field field) {
        return !field.getType().isPrimitive();
    }
}
