package com.melt.core.injector;

import com.melt.config.BeanInfo;
import com.melt.config.property.BeanProperty;
import com.melt.core.BeansContainer;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Lists.newArrayList;

public class PropertyInjector implements Injector {
    @Override
    public void inject(BeansContainer beansContainer, List<BeanInfo> beanInfos) {
        for (BeanInfo beanInfo : beanInfos) {
            injectProperties(beansContainer, beanInfo);
        }
    }

    private void injectProperties(BeansContainer beansContainer, BeanInfo beanInfo) {
        for (BeanProperty beanProperty : beanInfo.getProperties()) {
            beanProperty.setPropertyValue(beansContainer);
        }
    }
}
