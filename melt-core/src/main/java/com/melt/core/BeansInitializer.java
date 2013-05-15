package com.melt.core;

import com.melt.config.BeanInfo;
import com.melt.config.InjectionContext;

import java.util.List;

public class BeansInitializer {

    private InitializedBeans initializedBeans;

    public InitializedBeans initialize(Container parentContainer, List<BeanInfo> beanInfos) {
        initializedBeans = new InitializedBeans();

        defaultConstructorInitializing(beanInfos);
        parameterConstructorInitializing(parentContainer, beanInfos);
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
            beanInfo.injectProperties(new InjectionContext(parentContainer, initializedBeans));
        }
    }

    private void parameterConstructorInitializing(Container parentContainer, List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            beanInfo.getConstructorParameters().initialize(new InjectionContext(parentContainer, initializedBeans));
        }
    }
}
