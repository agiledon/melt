package com.github.agiledon.melt.config.autowired;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;

public interface AutoWired {
    void autoWired(InjectionContext injectionContext, BeanInfo beanInfo);
}
