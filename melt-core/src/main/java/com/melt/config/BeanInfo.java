package com.melt.config;

import com.melt.config.constructor.ConstructorParameters;
import com.melt.config.property.BeanProperty;
import com.melt.core.BeansContainer;

import java.util.ArrayList;
import java.util.List;

public class BeanInfo {
    private String name;
    private Class clazz;
    private Scope scope;
    private AutoWired autoWired;
    private ConstructorParameters constructorParameters;
    private List<BeanProperty> properties = new ArrayList<BeanProperty>();

    public BeanInfo(String name, Class clazz, Scope scope) {
        this.name = name;
        this.clazz = clazz;
        this.scope = scope;
    }

    public BeanInfo(String name, Class clazz) {
        this(name, clazz, Scope.SINGLETON);
    }

    public void injectProperty(BeansContainer beansContainer) {
        for (BeanProperty beanProperty : getProperties()) {
            beanProperty.setPropertyValue(beansContainer);
        }
    }

    public BeanInfo(String name, ConstructorParameters constructorParameters) {
        this.name = name;
        this.constructorParameters = constructorParameters;
    }

    public BeanInfo(Class clazz, ConstructorParameters constructorParameters) {
        this.clazz = clazz;
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

    public Class getClazz() {
        return clazz;
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
