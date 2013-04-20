package com.melt.config;

import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.property.BeanProperty;
import com.melt.core.BeansContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BeanInfo {
    private String name;
    private Class clazz;
    private AutoWiredBy autoWiredBy;
    private List<ConstructorParameter> constructorParameters = newArrayList();
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

    public void injectProperties(BeansContainer beansContainer) {
        autoWiredProperties(beansContainer);
        for (BeanProperty beanProperty : getProperties()) {
            beanProperty.injectPropertyValue(beansContainer);
        }
    }

    public void autoWiredProperties(BeansContainer beansContainer) {
        autoWiredBy.autoWired().autoWired(beansContainer, this);
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

    public List<ConstructorParameter> getConstructorParameters() {
        return constructorParameters;
    }

    public void setAutoWiredBy(AutoWiredBy autoWiredBy) {
        this.autoWiredBy = autoWiredBy;
    }
}
