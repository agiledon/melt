package com.github.agiledon.melt.config.property;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;
import static com.github.agiledon.melt.util.BeanLoader.loadReferenceBean;

public class BeanRefProperty extends BeanProperty {
    private String ref;

    public BeanRefProperty(BeanInfo beanInfo, String name, String ref) {
        super(beanInfo, name);
        this.ref = ref;
    }

    @Override
    protected Object getValue(InjectionContext injectionContext) {
        return loadReferenceBean(injectionContext, ref);
    }

}
