package com.melt.core.injector;

import com.melt.config.BeanInfo;

import java.util.List;
import java.util.Map;

public interface Injector {
    void inject(Map<String, Object> beans, List<BeanInfo> beanInfos);
}
