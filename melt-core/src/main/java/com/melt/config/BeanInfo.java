package com.melt.config;

import com.melt.config.property.BeanProperty;

import java.util.ArrayList;
import java.util.List;

public class BeanInfo {
    private String name;
    private String className;
    private Scope scope;
    private ConstructorFields constructorFields;
    private List<BeanProperty> properties = new ArrayList<BeanProperty>();

    public BeanInfo(String name, String className, Scope scope) {
        this.name = name;
        this.className = className;
        this.scope = scope;
    }

    public BeanInfo(String name, String className) {
        this(name, className, Scope.SINGLETON);
    }

    public BeanInfo(String name, ConstructorFields constructorFields) {
        this.name = name;
        this.constructorFields = constructorFields;
    }

    public boolean isNotDefaultConstructorBean() {
        return constructorFields == null ? true : false;
    }

    public void addProperty(BeanProperty beanProperty) {
        this.properties.add(beanProperty);
    }

    public List<BeanProperty> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public Scope getScope() {
        return scope;
    }

    public ConstructorFields getConstructorFields() {
        return constructorFields;
    }

    public void setConstructorFields(ConstructorFields constructorFields) {
        this.constructorFields = constructorFields;
    }
}
