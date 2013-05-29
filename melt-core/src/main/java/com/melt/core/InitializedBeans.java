package com.melt.core;

import com.google.common.collect.Iterators;
import com.melt.config.BeanInfo;
import com.melt.exceptions.MoreThanOneBeanWithSameClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class InitializedBeans {
    private final Map<String, Object> beansWithBeanName = newHashMap();
    private final Map<Class, Map<String, Object>> beansWithClass = newHashMap();
    private Logger logger = LoggerFactory.getLogger(InitializedBeans.class);

    public void addBean(String beanName, Object bean) {
        beansWithBeanName.put(beanName, bean);
    }

    public void addBean(String beanName, List<Class> classes, Object bean) {
        for (Class clazz : classes) {
            addBean(beanName, clazz, bean);
        }
    }

    public void addBean(String beanName, Class clazz, Object bean) {
        if (clazz == null || clazz == Object.class) {
            return;
        }
        Map<String, Object> beans = null;
        beans = beansWithClass.get(clazz);
        if (beans == null) {
            beans = newHashMap();
            beansWithClass.put(clazz, beans);
        }
        beans.put(beanName, bean);
        addBean(beanName, newArrayList(clazz.getInterfaces()), bean);
        addBean(beanName, clazz.getSuperclass(), bean);
    }

    public void addBean(BeanInfo beanInfo, Object bean) {
        addBean(beanInfo.getName(), bean);
        addBean(beanInfo.getName(), bean.getClass(), bean);
    }

    public Object getBean(String name) {
        return beansWithBeanName.get(name);
    }

    public <T> T getBean(Class T) {
        Map<String, Object> beans = beansWithClass.get(T);
        if (beans == null) {
            return null;
        }
        Collection<Object> values = beans.values();
        if (values.size() > 1) {
            String message = String.format("The %s more than one instance", T.getName());
            logger.error(message);
            throw new MoreThanOneBeanWithSameClass(message);
        }
        return (T)Iterators.get(values.iterator(), 0);
    }
}
