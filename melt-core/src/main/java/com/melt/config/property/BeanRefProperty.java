package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public class BeanRefProperty extends BeanProperty {
    private String ref;

    public BeanRefProperty(BeanInfo beanInfo, String name, String ref) {
        super(beanInfo, name);
        this.ref = ref;
    }

    @Override
    protected Object getValue(BeansContainer beansContainer) {
        return beansContainer.resolve(ref);
    }
}
