package com.melt.core;

import com.melt.bean.AutoWiredBy;
import com.melt.bean.BeanInfo;
import com.melt.bean.constructor.*;
import com.melt.bean.property.*;
import com.melt.exceptions.BeanConfigurationException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ContainerBuilder {
    private final BeansInitializer beansInitializer = new BeansInitializer();
    private final AutoWiredBy globalAutoWiredBy;
    private List<BeanInfo> beans = newArrayList();
    private final Container container = new Container();
    private BeanInfo currentBean = null;
    private Container parentContainer;

    public ContainerBuilder() {
        this(AutoWiredBy.NULL);
    }

    public ContainerBuilder(AutoWiredBy by) {
        this.globalAutoWiredBy = by;
    }

    public Container build() {
        InitializedBeans initializedBeans = beansInitializer.initialize(beans, parentContainer);
        container.setInitializedBeans(initializedBeans);
        container.setParentContainer(parentContainer);
        return container;
    }

    public <T> ContainerBuilder register(Class<T> registeredClass) {
        BeanInfo registeredBean = new BeanInfo(getBeanName(registeredClass), registeredClass);
        registeredBean.setAutoWiredBy(globalAutoWiredBy);
        beans.add(registeredBean);
        currentBean = registeredBean;
        ConstructorIndexer.reset();
        return this;
    }

    public <T> ContainerBuilder construct(Class<T> constructorParameterClass) {
        ConstructorParameter constructorParameter = new RefConstructorParameter(
                ConstructorIndexer.index(), constructorParameterClass.getSimpleName());
        if (currentBean != null) {
            currentBean.addConstructorParameter(constructorParameter);
            registerBeanInfoWithClass(constructorParameterClass);
        } else {
            throw new BeanConfigurationException("Didn't register main bean");
        }
        return this;
    }

    public ContainerBuilder construct(int paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder construct(long paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder construct(float paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder construct(double paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder construct(String paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder construct(Object paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    private void addConstructorParameter(ConstructorParameter constructorParameter) {
        validateBeanIsRegistered();
        currentBean.addConstructorParameter(constructorParameter);
    }

    private void validateBeanIsRegistered() {
        if (currentBean == null) {
            throw new BeanConfigurationException("Didn't register main bean");
        }
    }

    public <T> ContainerBuilder parent(Container container) {
        this.parentContainer = container;
        return this;
    }

    public ContainerBuilder autoWiredBy(AutoWiredBy by) {
        currentBean.setAutoWiredBy(by);
        return this;
    }

    public <T> ContainerBuilder withClass(Class<T> propertyClass) {
        validateIsInterface(propertyClass);
        addProperty(getBeanName(propertyClass), getBeanName(propertyClass));
        registerBeanInfoWithClass(propertyClass);
        ConstructorIndexer.reset();
        return this;
    }

    public ContainerBuilder withName(String beanName) {
        addProperty(beanName, beanName);
        ConstructorIndexer.reset();
        return this;
    }

    private <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            throw new BeanConfigurationException(String.format("%s can't be interface type.", propertyClass.getName()));
        }
    }

    public ContainerBuilder withValue(String propertyName, int propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withValue(String propertyName, double propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withValue(String propertyName, float propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withValue(String propertyName, long propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withValue(String propertyName, String propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withValue(String propertyName, Object propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    private void addPropertyAndResetConstructorIndexer(BeanProperty property) {
        currentBean.addProperty(property);
        ConstructorIndexer.reset();
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

    private <T> void addProperty(String beanName, String refName) {
        BeanProperty property = new BeanRefProperty(
                currentBean,
                beanName,
                refName);
        currentBean.addProperty(property);

    }

    private <T> boolean matchInterfaceClassName(Class<T> registeredClass, Class<?> anInterface) {
        return registeredClass.getSimpleName().toLowerCase().contains(anInterface.getSimpleName().toLowerCase());
    }

    private <T> boolean registerBeanInfoWithClass(Class<T> propertyClass) {
        return beans.add(new BeanInfo(getBeanName(propertyClass), propertyClass));
    }

    public ContainerBuilder asName(String beanName) {
        validateBeanIsRegistered();
        currentBean.setName(beanName);
        return this;
    }

    private static class ConstructorIndexer {
        private static int index = 0;

        public static int index() {
            return index++;
        }

        public static void reset() {
            index = 0;
        }
    }
}