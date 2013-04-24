package com.melt.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.melt.config.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.config.constructor.*;
import com.melt.config.property.*;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.exceptions.MoreThanOneClassRegisteredException;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ContainerBuilder {
    private final BeansInitializer beansInitializer = new BeansInitializer();
    private final AutoWiredBy globalAutoWiredBy;
    private List<BeanInfo> beans = newArrayList();
    private final Container container = new Container();
    private BeanInfo currentBean = null;
    private Container parentContainer;
    private Map<Class, List<BeanInfo>> classBeanInfoMap = newHashMap();
    private Class latestRegisteredClass;

    public ContainerBuilder() {
        this(AutoWiredBy.NULL);
    }

    public ContainerBuilder(AutoWiredBy by) {
        this.globalAutoWiredBy = by;
    }

    public Container build() {
        validateMoreThanOneClassRegistered();
        InitializedBeans initializedBeans = beansInitializer.initialize(beans, parentContainer);
        container.setInitializedBeans(initializedBeans);
        container.setParentContainer(parentContainer);
        return container;
    }

    public <T> ContainerBuilder register(Class<T> registeredClass) {
        validateIsInterface(registeredClass);
        validateMoreThanOneClassRegistered();
        BeanInfo registeredBean = new BeanInfo(getBeanName(registeredClass), registeredClass);
        registeredBean.setAutoWiredBy(globalAutoWiredBy);
        beans.add(registeredBean);
        currentBean = registeredBean;
        ConstructorIndexer.reset();
        addClassAndBeanInfoForValidate(registeredClass);
        return this;
    }

    public <T> ContainerBuilder withConstructorParameter(Class<T> constructorParameterClass) {
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


    public ContainerBuilder withRefConstructorParameter(String beanName) {
        addConstructorParameter(new RefConstructorParameter(
                ConstructorIndexer.index(), beanName));
        return this;
    }

    public ContainerBuilder withConstructorParameter(int paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder withConstructorParameter(long paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder withConstructorParameter(float paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder withConstructorParameter(double paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder withConstructorParameter(String paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public ContainerBuilder withConstructorParameter(Object paraValue) {
        addConstructorParameter(new GenericConstructorParameter(
                ConstructorIndexer.index(), paraValue));
        return this;
    }

    public <T> ContainerBuilder parent(Container container) {
        this.parentContainer = container;
        return this;
    }

    public ContainerBuilder autoWiredBy(AutoWiredBy by) {
        currentBean.setAutoWiredBy(by);
        return this;
    }

    public <T> ContainerBuilder withProperty(Class<T> propertyClass) {
        validateIsInterface(propertyClass);
        addProperty(getBeanName(propertyClass), getBeanName(propertyClass));
        registerBeanInfoWithClass(propertyClass);
        ConstructorIndexer.reset();
        return this;
    }

    public ContainerBuilder withRefProperty(String beanName) {
        addProperty(beanName, beanName);
        ConstructorIndexer.reset();
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, int propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, double propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, float propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, long propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, String propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder withProperty(String propertyName, Object propertyValue) {
        addPropertyAndResetConstructorIndexer(new GenericBeanProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContainerBuilder asName(String beanName) {
        validateBeanIsRegistered();
        currentBean.setName(beanName);
        return this;
    }

    public ContainerBuilder factory(String factoryMethod) {
        currentBean.setFactoryMethod(factoryMethod);
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

    private <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            throw new BeanConfigurationException(String.format("%s can't be interface type.", propertyClass.getName()));
        }
    }

    private <T> boolean matchInterfaceClassName(Class<T> registeredClass, Class<?> anInterface) {
        return registeredClass.getSimpleName().toLowerCase().contains(anInterface.getSimpleName().toLowerCase());
    }

    private <T> void addClassAndBeanInfoForValidate(Class<T> registeredClass) {
        latestRegisteredClass = registeredClass;
        List<BeanInfo> beanInfos = classBeanInfoMap.get(registeredClass);
        if (beanInfos == null) {
            beanInfos = newArrayList();
            classBeanInfoMap.put(registeredClass, beanInfos);
        }
        beanInfos.add(currentBean);
    }

    private <T> boolean registerBeanInfoWithClass(Class<T> propertyClass) {
        return beans.add(new BeanInfo(getBeanName(propertyClass), propertyClass));
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

    private static class ConstructorIndexer {
        private static int index = 0;

        public static int index() {
            return index++;
        }

        public static void reset() {
            index = 0;
        }
    }

    private <T> void validateMoreThanOneClassRegistered() {
        if (latestRegisteredClass != null && classBeanInfoMap.containsKey(latestRegisteredClass)) {
            List<BeanInfo> beanInfos = classBeanInfoMap.get(latestRegisteredClass);
            if (beanInfos.size() < 2) {
                return;
            }
            ImmutableSet<String> beanNames = from(beanInfos).transform(new Function<BeanInfo, String>() {
                @Override
                public String apply(BeanInfo beanInfo) {
                    return beanInfo.getName();
                }
            }).toSet();
            if (beanNames.size() != beanInfos.size()) {
                throw new MoreThanOneClassRegisteredException(String.format("%s has been already registered, please use asName to assign a name to this bean", latestRegisteredClass));
            }
        }
    }
}