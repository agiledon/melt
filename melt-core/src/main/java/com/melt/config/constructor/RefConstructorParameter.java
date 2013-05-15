package com.melt.config.constructor;

import com.melt.core.InjectionContext;
import com.melt.util.BeanLoader;

public class RefConstructorParameter extends ConstructorParameter {
    private String ref;

    public RefConstructorParameter(int index, String ref) {
        super(index);
        this.ref = ref;
    }

    @Override
    public void updateValue(InjectionContext injectionContext) {
        Object bean = BeanLoader.loadReferenceBean(injectionContext, ref);
        setValue(bean);
    }
}
