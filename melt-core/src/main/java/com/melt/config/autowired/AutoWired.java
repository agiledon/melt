package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

public interface AutoWired {
    void autoWired(BeansContainer beansContainer, BeanInfo beanInfo);
}
