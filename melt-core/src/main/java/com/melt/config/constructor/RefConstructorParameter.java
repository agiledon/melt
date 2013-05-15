package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public class RefConstructorParameter extends ConstructorParameter {
    private String ref;

    public RefConstructorParameter(int index, String ref) {
        super(index);
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public void updateValue(Container parentContainer, InitializedBeans initializedBeans) {
        Object bean = initializedBeans.getBean(getRef());
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(getRef());
        }
        setValue(bean);
    }
}
