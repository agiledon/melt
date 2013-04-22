package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public interface AutoWired {
    void autoWired(Container parentContainer, InitializedBeans initializedBeans, BeanInfo beanInfo);
}
