package com.melt.core;

import com.melt.bean.BeanInfo;

import java.util.List;

public class BeansInitializer {

    private InitializedBeans initializedBeans;

    public InitializedBeans initialize(List<BeanInfo> beanInfos, Container parentContainer) {
        initializedBeans = new InitializedBeans();

        defaultConstructorInitializing(beanInfos);
        parameterConstructorInitializing(beanInfos);
        propertyInitializing(parentContainer, beanInfos);

        return initializedBeans;
    }

    private void defaultConstructorInitializing(List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            Object bean = beanInfo.initialize();
            if (bean != null) {
                initializedBeans.addBean(beanInfo, bean);
            }
        }
    }

    private void propertyInitializing(Container parentContainer, List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            beanInfo.injectProperties(parentContainer, initializedBeans);
        }
    }

    private void parameterConstructorInitializing(List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            beanInfo.getConstructorParameters().initialize(initializedBeans);
        }
    }
}
