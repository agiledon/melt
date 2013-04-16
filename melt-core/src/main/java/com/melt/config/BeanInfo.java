package com.melt.config;

import com.melt.config.constructor.ConstructorParameters;
import com.melt.config.property.BeanProperty;

import java.util.ArrayList;
import java.util.List;

public class BeanInfo {
    private String name;
    private String className;
    private Scope scope;
    private ConstructorParameters constructorParameters;
    private List<BeanProperty> properties = new ArrayList<BeanProperty>();

    public BeanInfo(String name, String className, Scope scope) {
        this.name = name;
        this.className = className;
        this.scope = scope;
    }

    public BeanInfo(String name, String className) {
        this(name, className, Scope.SINGLETON);
    }

    public BeanInfo(String name, ConstructorParameters constructorParameters) {
        this.name = name;
        this.constructorParameters = constructorParameters;
    }

    public boolean isNotDefaultConstructorBean() {
        return constructorParameters == null;
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

    public ConstructorParameters getConstructorParameters() {
        return constructorParameters;
    }

    public void setConstructorParameters(ConstructorParameters constructorParameters) {
        this.constructorParameters = constructorParameters;
    }
}
