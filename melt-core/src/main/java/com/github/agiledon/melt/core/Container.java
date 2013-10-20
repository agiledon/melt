package com.github.agiledon.melt.core;

public class Container {

    private InitializedBeans initializedBeans;
    private Container parentContainer;

    protected Container(InjectionModule module) {
        initializedBeans = module.build(null);
    }

    public Container(InjectionModule module, Container parentContainer) {
        this.parentContainer = parentContainer;
        initializedBeans = module.build(parentContainer);
    }

    public <T> T resolve(Class targetClass) {
        T bean = initializedBeans.getBean(targetClass);
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(targetClass);
        }
        return bean;
    }

    public <T> T resolve(String beanName) {
        T bean = (T) initializedBeans.getBean(beanName);
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(beanName);
        }
        return bean;
    }
}
