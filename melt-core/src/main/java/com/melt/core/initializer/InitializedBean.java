package com.melt.core.initializer;

import java.util.List;

public class InitializedBean {
    private Object bean;
    private List<Class> types;

    public InitializedBean(Object bean, List<Class> types) {
        this.bean = bean;
        this.types = types;
    }

    public Object getBean() {
        return bean;
    }

    public List<Class> getTypes() {
        return types;
    }
}
