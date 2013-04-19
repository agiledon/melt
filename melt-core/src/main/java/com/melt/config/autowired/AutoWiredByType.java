package com.melt.config.autowired;

import com.melt.core.BeansContainer;

import java.lang.reflect.Field;

public class AutoWiredByType extends AbstractAutoWiredBy {
    @Override
    protected Object getValue(BeansContainer beansContainer, Field field) {
        return beansContainer.resolve(field.getType());
    }
}
