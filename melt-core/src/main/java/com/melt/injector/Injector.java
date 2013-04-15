package com.melt.injector;

import com.melt.config.BeanConfig;

import java.util.List;
import java.util.Map;

public interface Injector {
    void inject(Map<String, Object> beans, List<BeanConfig> beanConfigs);
}
