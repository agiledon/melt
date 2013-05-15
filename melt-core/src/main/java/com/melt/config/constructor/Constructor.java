package com.melt.config.constructor;

import com.melt.config.BeanInfo;
import com.melt.config.InjectionContext;
import com.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.primitives.Primitives.isWrapperType;
import static com.google.common.primitives.Primitives.unwrap;
import static com.melt.util.ReflectionUtil.findConstructor;

public class Constructor {
    private List<ConstructorParameter> parameters = newArrayList();
    private BeanInfo beanInfo;
    private Logger logger = LoggerFactory.getLogger(ConstructorParameter.class);

    public Constructor(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public void addParameter(ConstructorParameter parameter) {
        parameters.add(parameter);
    }

    public void initialize(InjectionContext injectionContext) {
        if (isDefaultConstructor()) {
            return;
        }

        Object targetBean = initializeTargetBean(injectionContext, beanInfo);
        injectionContext.getInitializedBeans().addBean(beanInfo, targetBean);
    }

    private Object initializeTargetBean(InjectionContext injectionContext, BeanInfo targetBean) {
        Class targetClass = targetBean.getClazz();

        Object[] parameterBeans = getParameterBeans(injectionContext);
        return createInstance(targetClass, parameterBeans);
    }

    private Object[] getParameterBeans(InjectionContext injectionContext) {
        Map<Integer, Object> parameterMap = newHashMap();
        for (ConstructorParameter parameter : parameters) {
            parameter.updateValue(injectionContext);
            parameterMap.put(parameter.getIndex(), parameter.getValue());
        }

        return parameterMap.values().toArray();
    }

    private <T> T createInstance(Class<T> targetClass, Object... dependencies) {
        String message = String.format("Can't initialize bean: %s", targetClass.getName());
        try {
            Class[] classes = getClassesFrom(dependencies);
            java.lang.reflect.Constructor<T> correctConstructor = findConstructor(targetClass, classes);
            return correctConstructor.newInstance(dependencies);
        } catch (NoSuchMethodException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        } catch (InvocationTargetException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        } catch (InstantiationException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        } catch (IllegalAccessException e) {
            logger.error(message);
            throw new InitBeanException(message, e);
        }
    }

    private Class[] getClassesFrom(Object[] dependencies) {
        List<Class> classes = newArrayList();
        for (Object dependency : dependencies) {
            if (isWrapperType(dependency.getClass())) {
                classes.add(unwrap(dependency.getClass()));
            } else {
                classes.add(dependency.getClass());
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public boolean isDefaultConstructor() {
        return parameters.size() == 0;
    }
}
