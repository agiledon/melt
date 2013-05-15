package com.melt.config.constructor;

import com.google.common.primitives.Primitives;
import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger = LoggerFactory.getLogger(ConstructorParameter.class);

    public ConstructorParameters(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public void addConstructorParameter(ConstructorParameter parameter) {
        parameters.add(parameter);
    }

    public List<ConstructorParameter> getConstructorParameters() {
        return parameters;
    }

    public void initialize(Container parentContainer, InitializedBeans container) {
        if (beanInfo.isDefaultConstructorBean()) {
            return;
        }

        Object targetBean = initializeTargetBean(parentContainer, container, beanInfo);
        container.addBean(beanInfo, targetBean);
    }

    private Object initializeTargetBean(Container parentContainer, InitializedBeans container, BeanInfo targetBean) {
        Class targetClass = targetBean.getClazz();

        Object[] parameterBeans = getParameterBeans(parentContainer, container);
        return createInstance(targetClass, parameterBeans);
    }

    private Object[] getParameterBeans(Container parentContainer, InitializedBeans container) {
        Map<Integer, Object> parameterMap = newHashMap();
        for (ConstructorParameter parameter : getConstructorParameters()) {
            int index = parameter.getIndex();
            if (parameter instanceof RefConstructorParameter) {
                RefConstructorParameter refParameter = (RefConstructorParameter) parameter;
                Object bean = container.getBean(refParameter.getRef());
                if (bean == null && parentContainer != null) {
                    bean = parentContainer.resolve(refParameter.getRef());
                }
                parameter.setValue(bean);
            }
            parameterMap.put(index, parameter.getValue());
        }

        return parameterMap.values().toArray();
    }

    private <T> T createInstance(Class<T> targetClass, Object... dependencies) {
        String message = String.format("Can't initialize bean: %s", targetClass.getName());
        try {
            Class[] classes = getClassesFrom(dependencies);
            Constructor<T> correctConstructor = findConstructor(targetClass, classes);
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
            if (Primitives.isWrapperType(dependency.getClass())) {
                classes.add(Primitives.unwrap(dependency.getClass()));
            } else {
                classes.add(dependency.getClass());
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }
}
