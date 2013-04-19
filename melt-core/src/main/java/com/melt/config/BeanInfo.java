package com.melt.config;

import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.property.BeanProperty;
import com.melt.core.BeansContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BeanInfo {
    private String name;
    private Class clazz;
    private Scope scope;
    private AutoWiredBy autoWiredBy;
    private List<ConstructorParameter> constructorParameters = newArrayList();
    private List<BeanProperty> properties = newArrayList();
    private boolean isInterfaceType = false;

    public BeanInfo(String name, Class clazz, Scope scope) {
        this.name = name;
        this.clazz = clazz;
        if (clazz.isInterface()) {
            isInterfaceType = true;
        }
        this.scope = scope;
    }

    public BeanInfo(String name, Class clazz) {
        this(name, clazz, Scope.SINGLETON);
    }

    public void injectProperties(BeansContainer beansContainer) {
        for (BeanProperty beanProperty : getProperties()) {
            beanProperty.injectPropertyValue(beansContainer);
        }
    }

    public boolean isInterface() {
        return isInterfaceType;
    }

    public boolean isDefaultConstructorBean() {
        return constructorParameters.size() == 0;
    }

    public void addProperty(BeanProperty beanProperty) {
        this.properties.add(beanProperty);
    }

    public void addConstructorParameter(ConstructorParameter constructorParameter) {
        this.constructorParameters.add(constructorParameter);
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

    public List<ConstructorParameter> getConstructorParameters() {
        return constructorParameters;
    }

    public AutoWiredBy getAutoWiredBy() {
        return autoWiredBy;
    }

    public void setAutoWiredBy(AutoWiredBy autoWiredBy) {
        this.autoWiredBy = autoWiredBy;
    }

    public void autoWiredProperties(BeansContainer beansContainer) {

    }
}
