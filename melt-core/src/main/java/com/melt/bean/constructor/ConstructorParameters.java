package com.melt.bean.constructor;

import com.google.common.primitives.Primitives;
import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;
import com.melt.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.melt.util.ReflectionUtil.findConstructor;

public class ConstructorParameters {
    private List<ConstructorParameter> parameters = newArrayList();
    private BeanInfo beanInfo;

    public ConstructorParameters(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public void addConstructorParameter(ConstructorParameter parameter) {
        parameters.add(parameter);
    }

    public List<ConstructorParameter> getConstructorParameters() {
        return parameters;
    }

    public BeanInfo getBeanInfo() {
        return beanInfo;
    }

    public void initialize(InitializedBeans container) {
        if (beanInfo.isDefaultConstructorBean()) {
            return;
        }

        Object targetBean = initializeTargetBean(container, beanInfo);
        container.addBean(beanInfo, targetBean);
    }

    private Object initializeTargetBean(InitializedBeans container, BeanInfo targetBean) {
        Class targetClass = targetBean.getClazz();

        Object[] parameterBeans = getParameterBeans(container);
        return createInstance(targetClass, parameterBeans);
    }

    private Object[] getParameterBeans(InitializedBeans container) {
        Map<Integer, Object> parameterMap = newHashMap();
        for (ConstructorParameter parameter : getConstructorParameters()) {
            int index = parameter.getIndex();
            if (parameter instanceof RefConstructorParameter) {
                RefConstructorParameter refParameter = (RefConstructorParameter) parameter;
                parameter.setValue(container.getBean(refParameter.getRef()));
            }
            parameterMap.put(index, parameter.getValue());
        }

        return parameterMap.values().toArray();
    }

    private <T> T createInstance(Class<T> targetClass, Object... dependencies) {
        try {
            Class[] classes = getClassesFrom(dependencies);
            Constructor<T> correctConstructor = findConstructor(targetClass, classes);
            return correctConstructor.newInstance(dependencies);
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
            if (Primitives.isWrapperType(dependency.getClass())) {
                classes.add(Primitives.unwrap(dependency.getClass()));
            } else {
                classes.add(dependency.getClass());
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }
}
