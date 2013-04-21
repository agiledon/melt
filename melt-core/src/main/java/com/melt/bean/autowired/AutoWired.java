package com.melt.bean.autowired;

import com.melt.bean.BeanInfo;
import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public interface AutoWired {
    void autoWired(Container parentContainer, InitializedBeans initializedBeans, BeanInfo beanInfo);
}
