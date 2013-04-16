package com.melt.core;

import com.google.common.collect.Iterators;
import com.melt.exceptions.MoreThanOneBeanWithSameClass;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class BeansContainer {
    private final Map<String, Object> beansWithBeanName = newHashMap();
    private final Map<String, Class> classesWithBeanName = newHashMap();
    private final Map<Class, Map<String, Object>> beansWithClass = newHashMap();

    public void addBean(String beanName, Object bean) {
        beansWithBeanName.put(beanName, bean);
    }

    public void addBean(Class clazz, String beanName, Object bean) {
        Map<String, Object> beans = null;
        beans = beansWithClass.get(clazz);
        if (beans == null) {
            beans = newHashMap();
            beansWithClass.put(clazz, beans);
        }
        beans.put(beanName, bean);
    }

    public Object get(String name) {
        return beansWithBeanName.get(name);
    }

    public Object get(Class clazz) {
        Map<String, Object> beans = beansWithClass.get(clazz);
        Collection<Object> values = beans.values();
        if (values.size() > 1) {
            throw new MoreThanOneBeanWithSameClass(clazz.getName());
        }
        return Iterators.get(values.iterator(), 0);
    }


    public void addClass(String beanName, Class clazz) {
        classesWithBeanName.put(beanName, clazz);
    }

    public Class getClazz(String beanName) {
        return classesWithBeanName.get(beanName);
    }
}
