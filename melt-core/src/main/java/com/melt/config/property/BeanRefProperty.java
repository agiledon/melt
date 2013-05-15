package com.melt.config.property;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.util.BeanLoader;

import static com.melt.util.BeanLoader.loadReferenceBean;

public class BeanRefProperty extends BeanProperty {
    private String ref;

    public BeanRefProperty(BeanInfo beanInfo, String name, String ref) {
        super(beanInfo, name);
        this.ref = ref;
    }

    @Override
    protected Object getValue(InitializedBeans initializedBeans, Container parentContainer) {
        return loadReferenceBean(initializedBeans, parentContainer, ref);
    }

}
