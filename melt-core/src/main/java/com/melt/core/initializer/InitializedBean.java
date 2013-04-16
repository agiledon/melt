package com.melt.core.initializer;

import java.util.List;

public class InitializedBean {
    private Object bean;
    private Class clazz;
    private List<Class> types;

    public InitializedBean(Object bean, Class clazz, List<Class> types) {
        this.bean = bean;
        this.clazz = clazz;
        this.types = types;
    }

    public Object getBean() {
        return bean;
    }

    public Class getClazz() {
        return clazz;
    }

    public List<Class> getTypes() {
        return types;
    }
}
