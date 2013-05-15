package com.melt.core;

import com.melt.config.autowired.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.constructor.GenericConstructorParameter;
import com.melt.config.constructor.RefConstructorParameter;
import com.melt.config.property.BeanProperty;
import com.melt.config.property.BeanRefProperty;
import com.melt.config.property.GenericBeanProperty;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.util.ConstructorIndexer;
import com.melt.util.InjectionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.melt.util.InjectionValidator.validateBeanIsRegistered;

public class NestedInjectionModule {
    private BeanInfo currentBean;
    private List<BeanInfo> beans;
    private Logger logger = LoggerFactory.getLogger(NestedInjectionModule.class);

    public NestedInjectionModule autoWiredBy(AutoWiredBy by) {
        currentBean.setAutoWiredBy(by);
        return this;
    }

    public NestedInjectionModule withRefConstructorParameter(String beanName) {
        addConstructorParameter(new RefConstructorParameter(
                ConstructorIndexer.index(), beanName));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(int paraValue) {
        addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(long paraValue) {
        addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(float paraValue) {
       addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(double paraValue) {
        addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(String paraValue) {
        addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public NestedInjectionModule withConstructorParameter(Object paraValue) {
        addConstructorParameter(createConstructorParameter(paraValue));
        return this;
    }

    public <T> NestedInjectionModule withConstructorParameter(Class<T> constructorParameterClass) {
        ConstructorParameter constructorParameter = new RefConstructorParameter(
                ConstructorIndexer.index(), constructorParameterClass.getSimpleName());
        if (this.currentBean != null) {
            this.currentBean.addConstructorParameter(constructorParameter);
            registerBeanInfoWithClass(constructorParameterClass, this.beans);
        } else {
            String message = "Didn't register main bean";
            logger.error(message);
            throw new BeanConfigurationException(message);
        }
        return this;
    }

    private <T> GenericConstructorParameter createConstructorParameter(T paraValue) {
        return new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue);
    }

    public NestedInjectionModule withRefProperty(String propertyName, String refBeanName) {
        addProperty(propertyName, refBeanName);
        ConstructorIndexer.reset();
        return this;
    }

    public <T> NestedInjectionModule withProperty(Class<T> propertyClass) {
        InjectionValidator.validateIsInterface(propertyClass);
        addProperty(getBeanName(propertyClass), getBeanName(propertyClass));
        registerBeanInfoWithClass(propertyClass, beans);
        ConstructorIndexer.reset();
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, int propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, double propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, float propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, long propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, String propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    public NestedInjectionModule withProperty(String propertyName, Object propertyValue) {
        addPropertyAndResetConstructorIndexer(createBeanProperty(propertyName, propertyValue));
        return this;
    }

    private <T> GenericBeanProperty createBeanProperty(String propertyName, T propertyValue) {
        return new GenericBeanProperty(currentBean, propertyName, propertyValue);
    }

    public NestedInjectionModule asName(String beanName) {
        validateBeanIsRegistered(this.currentBean);
        currentBean.setName(beanName);
        return this;
    }


    public NestedInjectionModule factory(String factoryMethod) {
        currentBean.setFactoryMethod(factoryMethod);
        return this;
    }

    private void addPropertyAndResetConstructorIndexer(BeanProperty property) {
        currentBean.addProperty(property);
        ConstructorIndexer.reset();
    }

    private <T> void addProperty(String beanName, String refName) {
        BeanProperty property = new BeanRefProperty(
                currentBean,
                beanName,
                refName);
        currentBean.addProperty(property);

    }

    private void addConstructorParameter(ConstructorParameter constructorParameter) {
        validateBeanIsRegistered(this.currentBean);
        this.currentBean.addConstructorParameter(constructorParameter);
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
