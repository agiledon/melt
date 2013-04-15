package com.melt.initializer;

import com.melt.config.BeanInfo;

public interface Initializer {
    Object initialize(BeanInfo beanConfig);
}
