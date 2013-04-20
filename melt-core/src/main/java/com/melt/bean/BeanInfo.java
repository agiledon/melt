package com.melt.bean;

import com.melt.bean.constructor.ConstructorParameter;
import com.melt.bean.constructor.ConstructorParameters;
import com.melt.bean.property.BeanProperty;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BeanInfo {
    private String name;
    private Class clazz;
    private AutoWiredBy autoWiredBy;
    private ConstructorParameters constructorParameters = new ConstructorParameters(this);
    private List<BeanProperty> properties = newArrayList();
    private boolean isInterfaceType = false;

    public BeanInfo(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
        if (clazz.isInterface()) {
            isInterfaceType = true;
        }
        this.autoWiredBy = AutoWiredBy.NULL;
    }

    public void injectProperties(InitializedBeans initializedBeans) {
        autoWiredProperties(initializedBeans);
        for (BeanProperty beanProperty : getProperties()) {
            beanProperty.injectPropertyValue(initializedBeans);
        }
    }

    public void autoWiredProperties(InitializedBeans initializedBeans) {
        autoWiredBy.autoWired().autoWired(initializedBeans, this);
    }

    public boolean isInterface() {
        return isInterfaceType;
    }

    public boolean isDefaultConstructorBean() {
        return constructorParameters.getConstructorParameters().size() == 0;
    }

    public void addProperty(BeanProperty beanProperty) {
        this.properties.add(beanProperty);
    }

    public void addConstructorParameter(ConstructorParameter constructorParameter) {
        this.constructorParameters.addConstructorParameter(constructorParameter);
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

    public ConstructorParameters getConstructorParameters() {
        return constructorParameters;
    }

    public void setAutoWiredBy(AutoWiredBy autoWiredBy) {
        this.autoWiredBy = autoWiredBy;
    }

    public Object initialize() {
        if (isDefaultConstructorBean()) {
            Class clazz = getClazz();
            return createBean(clazz);
        }
        return null;
    }

    private Object createBean(Class clazz) {
        String className = clazz.getName();
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        } catch (IllegalAccessException e) {
            throw new InitBeanException(String.format("Can't initialize bean: %s", className), e);
        }
    }
}
