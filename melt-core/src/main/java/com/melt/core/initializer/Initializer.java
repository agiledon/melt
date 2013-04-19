package com.melt.core.initializer;

import com.melt.config.BeanInfo;

public interface Initializer {
    Object initialize(BeanInfo beanInfo);
}
