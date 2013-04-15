package com.melt.initializer;

import com.melt.config.BeanConfig;
import com.melt.config.Configs;

import java.util.Map;

public interface Initializer {
    Object initialize(BeanConfig beanConfig);
}
