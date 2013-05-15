package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public interface ParameterValueUpdater {
    void updateValue(InitializedBeans container, Container parentContainer);
}
