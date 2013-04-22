package com.melt.core;

public class Container {

    private InitializedBeans initializedBeans;
    private Container parentContainer;

    public <T> T resolve(Class targetClass) {
        T bean = initializedBeans.getBean(targetClass);
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(targetClass);
        }
        return bean;
    }

    public <T> T resolve(String beanName) {
        T bean = (T) initializedBeans.getBean(beanName);
        if (bean == null) {
            bean = parentContainer.resolve(beanName);
        }
        return bean;
    }

    public void setInitializedBeans(InitializedBeans initializedBeans) {
        this.initializedBeans = initializedBeans;
    }

    public void setParentContainer(Container parentContainer) {
        this.parentContainer = parentContainer;
    }
}
