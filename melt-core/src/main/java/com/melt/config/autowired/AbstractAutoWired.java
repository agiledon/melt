package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.InjectionContext;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.AutoWiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class AbstractAutoWired implements AutoWired {
    private Logger logger = LoggerFactory.getLogger(AbstractAutoWired.class);
    @Override
    public void autoWired(InjectionContext injectionContext, BeanInfo beanInfo) {
        Field[] fields = beanInfo.getClazz().getDeclaredFields();
        Object bean = injectionContext.getInitializedBeans().getBean(beanInfo.getName());
        for (Field field : fields) {
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
        return !field.getType().isPrimitive();
    }

    protected abstract Object resolveBean(Container parentContainer, Field field);

    protected abstract Object getBean(InitializedBeans initializedBeans, Field field);
}
