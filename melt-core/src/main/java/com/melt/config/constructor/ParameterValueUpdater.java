package com.melt.config.constructor;

import com.melt.config.InjectionContext;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public interface ParameterValueUpdater {
    void updateValue(InjectionContext injectionContext);
}
