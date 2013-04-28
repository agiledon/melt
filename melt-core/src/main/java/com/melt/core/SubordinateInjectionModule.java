package com.melt.core;

import com.melt.config.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.constructor.GenericConstructorParameter;
import com.melt.config.constructor.RefConstructorParameter;
import com.melt.config.property.BeanProperty;
import com.melt.config.property.BeanRefProperty;
import com.melt.config.property.GenericBeanProperty;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.util.ConstructorIndexer;

import java.util.List;

public class SubordinateInjectionModule {

    private BeanInfo currentBean;
    private List<BeanInfo> beans;

    public SubordinateInjectionModule withConstructorParameter(int paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule withConstructorParameter(long paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule withRefConstructorParameter(String beanName) {
        addConstructorParameter(new RefConstructorParameter(
                ConstructorIndexer.index(), beanName));
        return this;
    }

    public SubordinateInjectionModule withConstructorParameter(float paraValue) {
       addConstructorParameter(new GenericConstructorParameter(
               ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule withConstructorParameter(double paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule withConstructorParameter(String paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule withConstructorParameter(Object paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public SubordinateInjectionModule autoWiredBy(AutoWiredBy by) {
        currentBean.setAutoWiredBy(by);
        return this;
    }

    public <T> SubordinateInjectionModule withProperty(Class<T> propertyClass) {
        validateIsInterface(propertyClass);
        addProperty(getBeanName(propertyClass), getBeanName(propertyClass));
        registerBeanInfoWithClass(propertyClass, beans);
        ConstructorIndexer.reset();
        return this;
    }

    public SubordinateInjectionModule withRefProperty(String propertyName, String refBeanName) {
        addProperty(propertyName, refBeanName);
        ConstructorIndexer.reset();
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, int propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, double propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, float propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, long propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, String propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule withProperty(String propertyName, Object propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public SubordinateInjectionModule asName(String beanName) {
        validateBeanIsRegistered();
        currentBean.setName(beanName);
        return this;
    }

    public SubordinateInjectionModule factory(String factoryMethod) {
        currentBean.setFactoryMethod(factoryMethod);
        return this;
    }


    private void addPropertyAndResetConstructorIndexer(BeanProperty property) {
        currentBean.addProperty(property);
        ConstructorIndexer.reset();
    }

    private <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            throw new BeanConfigurationException(String.format("%s can't be interface type.", propertyClass.getName()));
        }
    }

    private <T> void addProperty(String beanName, String refName) {
        BeanProperty property = new BeanRefProperty(
                currentBean,
                beanName,
                refName);
        currentBean.addProperty(property);

    }

    private void addConstructorParameter(ConstructorParameter constructorParameter) {
        validateBeanIsRegistered();
        this.currentBean.addConstructorParameter(constructorParameter);
    }

    private void validateBeanIsRegistered() {
        if (this.currentBean == null) {
            throw new BeanConfigurationException("Didn't register main bean");
        }
    }

    public <T> SubordinateInjectionModule withConstructorParameter(Class<T> constructorParameterClass) {
        ConstructorParameter constructorParameter = new RefConstructorParameter(
                ConstructorIndexer.index(), constructorParameterClass.getSimpleName());
        if (this.currentBean != null) {
            this.currentBean.addConstructorParameter(constructorParameter);
            registerBeanInfoWithClass(constructorParameterClass, this.beans);
        } else {
            throw new BeanConfigurationException("Didn't register main bean");
        }
        return this;
    }

    private <T> String getBeanName(Class<T> registeredClass) {
        Class<?>[] interfaces = registeredClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                if (matchInterfaceClassName(registeredClass, anInterface)) {
                    return anInterface.getSimpleName();
                }
            }
        }
        return registeredClass.getSimpleName();
    }

    private <T> boolean matchInterfaceClassName(Class<T> registeredClass, Class<?> anInterface) {
        return registeredClass.getSimpleName().toLowerCase().contains(anInterface.getSimpleName().toLowerCase());
    }

    private <T> boolean registerBeanInfoWithClass(Class<T> propertyClass, List<BeanInfo> beans) {
        return beans.add(new BeanInfo(getBeanName(propertyClass), propertyClass));
    }

    public void setCurrentBean(BeanInfo currentBean) {
        this.currentBean = currentBean;
    }

    public void setBeans(List<BeanInfo> beans) {
        this.beans = beans;
    }
}
