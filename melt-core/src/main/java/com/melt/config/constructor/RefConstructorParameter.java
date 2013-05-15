package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.util.BeanLoader;

public class RefConstructorParameter extends ConstructorParameter {
    private String ref;

    public RefConstructorParameter(int index, String ref) {
        super(index);
        this.ref = ref;
    }

    @Override
    public void updateValue(InitializedBeans initializedBeans, Container parentContainer) {
        Object bean = BeanLoader.loadReferenceBean(initializedBeans, parentContainer, ref);
        setValue(bean);
    }
}
