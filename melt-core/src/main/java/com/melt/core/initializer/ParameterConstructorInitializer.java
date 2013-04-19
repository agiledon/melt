package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.core.BeansContainer;
import com.melt.exceptions.InitBeanException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ParameterConstructorInitializer {
    public void initialize(BeansContainer container, BeanInfo targetBean) {
        if (targetBean.isDefaultConstructorBean()) {
            return;
        }

        Class targetClass = targetBean.getClazz();
        List<ConstructorParameter> constructorParameters = targetBean.getConstructorParameters();

        Object[] parameterBeans = getParameterBeans(container, constructorParameters);
        Object target = createInstance(targetClass, parameterBeans);
        if (targetBean.isInterface()) {
            container.addBean(targetBean.getName(), targetBean.getClazz(), target);
        } else {
            container.addBean(targetBean.getName(), target);
        }
    }

    private Object[] getParameterBeans(BeansContainer container, List<ConstructorParameter> constructorParameters) {
        Map<Integer, Object> parameterMap = newHashMap();
        for (ConstructorParameter parameter : constructorParameters) {
            int index = parameter.getIndex();
            Object parameterValue = container.resolve(parameter.getRef());
            parameterMap.put(index, parameterValue);
        }

        return parameterMap.values().toArray();
    }

    private <T> T createInstance(Class<T> targetClass, Object... dependencies) {
        try {
            Class[] classes = getClassesFrom(dependencies);
            Constructor<T> constructor = targetClass.getConstructor(classes);
            return constructor.newInstance(dependencies);
        } catch (NoSuchMethodException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", targetClass.getName()), e);
        } catch (InvocationTargetException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", targetClass.getName()), e);
        } catch (InstantiationException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", targetClass.getName()), e);
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", targetClass.getName()), e);
        }
    }

    private Class[] getClassesFrom(Object[] dependencies) {
        List<Class> classes = newArrayList();
        for (Object dependency : dependencies) {
            classes.add(dependency.getClass());
        }
        return classes.toArray(new Class[classes.size()]);
    }
}
