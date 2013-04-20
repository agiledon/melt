package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

import java.util.List;

public class ContainerInitializer {

    private final DefaultConstructorInitializer defaultConstructorInitializer;
    private final ParameterConstructorInitializer parameterConstructorInitializer;

    public ContainerInitializer() {
        defaultConstructorInitializer = new DefaultConstructorInitializer();
        parameterConstructorInitializer = new ParameterConstructorInitializer();
    }

    public BeansContainer initialize(List<BeanInfo> beanInfos) {
        BeansContainer container = new BeansContainer();

        defaultConstructorInitializing(beanInfos, container);
        parameterConstructorInitializing(beanInfos, container);
        propertyInitializing(beanInfos, container);

        return container;
    }

    private void defaultConstructorInitializing(List<BeanInfo> beanInfos, BeansContainer container) {
        for (BeanInfo beanInfo : beanInfos) {
            Object bean = defaultConstructorInitializer.initialize(beanInfo);
            if (bean != null) {
                container.addBean(beanInfo.getName(), beanInfo.getClazz(), bean);
                container.addBean(beanInfo.getName(), bean);
            }
        }

    }

    private void propertyInitializing(List<BeanInfo> beanInfos, BeansContainer container) {
        for (BeanInfo beanInfo : beanInfos) {
            beanInfo.injectProperties(container);
        }
    }

    private void parameterConstructorInitializing(List<BeanInfo> beanInfos, BeansContainer container) {
        for (BeanInfo beanInfo : beanInfos) {
            parameterConstructorInitializer.initialize(container, beanInfo);
        }
    }
}
