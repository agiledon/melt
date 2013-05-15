package com.melt.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.melt.config.autowired.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.exceptions.MoreThanOneClassRegisteredException;
import com.melt.util.ConstructorIndexer;
import com.melt.util.InjectionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger = LoggerFactory.getLogger(InjectionModule.class);

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
        return  beansInitializer.initialize(parentContainer, beans);
    }

    public <T> SubordinateInjectionModule register(Class<T> registeredClass) {
        validateInjection(registeredClass);
        registerBeanAndUpdateCurrentBean(registeredClass);
        addClassAndBeanInfoForValidate(registeredClass);

        ConstructorIndexer.reset();
        updateSubordinateModule();

        return subordinateModule;
    }

    private <T> void registerBeanAndUpdateCurrentBean(Class<T> registeredClass) {
        BeanInfo registeredBean = new BeanInfo(getBeanName(registeredClass), registeredClass);
        registeredBean.setAutoWiredBy(globalAutoWiredBy);
        beans.add(registeredBean);
        currentBean = registeredBean;
    }

    private <T> void validateInjection(Class<T> registeredClass) {
        InjectionValidator.validateIsInterface(registeredClass);
        validateMoreThanOneClassRegistered();
    }

    private void updateSubordinateModule() {
        subordinateModule.setCurrentBean(currentBean);
        subordinateModule.setBeans(beans);
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
                String message = String.format("%s has been already registered, please use asName to assign a name to this bean", latestRegisteredClass);
                logger.error(message);
                throw new MoreThanOneClassRegisteredException(message);
            }
        }
    }
}