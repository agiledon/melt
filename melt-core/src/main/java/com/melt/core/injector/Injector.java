package com.melt.core.injector;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

import java.util.List;
import java.util.Map;

public interface Injector {
    void inject(BeansContainer beansContainer, List<BeanInfo> beanInfos);
}
