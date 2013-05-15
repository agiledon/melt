package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.InjectionContext;

public interface AutoWired {
    void autoWired(InjectionContext injectionContext, BeanInfo beanInfo);
}
