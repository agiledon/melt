package com.melt.config;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public class InjectionContext {
    private final Container parentContainer;
    private final InitializedBeans initializedBeans;

    public InjectionContext(Container parentContainer, InitializedBeans initializedBeans) {
        this.parentContainer = parentContainer;
        this.initializedBeans = initializedBeans;
    }

    public Container getParentContainer() {
        return parentContainer;
    }

    public InitializedBeans getInitializedBeans() {
        return initializedBeans;
    }
}
