package com.melt.bean.autowired;

import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;

public interface AutoWired {
    void autoWired(InitializedBeans initializedBeans, BeanInfo beanInfo);
}
