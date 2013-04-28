package com.melt.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.melt.config.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.exceptions.MoreThanOneClassRegisteredException;
import com.melt.util.ConstructorIndexer;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public abstract class InjectionModule {
    private final BeansInitializer beansInitializer = new BeansInitializer();
    private final AutoWiredBy globalAutoWiredBy;
    private List<BeanInfo> beans = newArrayList();
    private BeanInfo currentBean = null;
    private Map<Class, List<BeanInfo>> classBeanInfoMap = newHashMap();
    private Class latestRegisteredClass;
    private SubordinateInjectionModule subordinateModule = new SubordinateInjectionModule();

    public InjectionModule() {
        this(AutoWiredBy.NULL);
    }

    public InjectionModule(AutoWiredBy by) {
        this.globalAutoWiredBy = by;
    }

    public abstract void configure();

    public InitializedBeans build(Container parentContainer) {
        configure();
        validateMoreThanOneClassRegistered();
        return  beansInitializer.initialize(beans, parentContainer);
    }

    public <T> SubordinateInjectionModule register(Class<T> registeredClass) {
        validateIsInterface(registeredClass);
        validateMoreThanOneClassRegistered();
        BeanInfo registeredBean = new BeanInfo(getBeanName(registeredClass), registeredClass);
        registeredBean.setAutoWiredBy(globalAutoWiredBy);
        beans.add(registeredBean);
        currentBean = registeredBean;
        ConstructorIndexer.reset();
        addClassAndBeanInfoForValidate(registeredClass);
        subordinateModule.setCurrentBean(currentBean);
        subordinateModule.setBeans(beans);

        return subordinateModule;
    }

    private  <T> String getBeanName(Class<T> registeredClass) {
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

    private <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            throw new BeanConfigurationException(String.format("%s can't be interface type.", propertyClass.getName()));
        }
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