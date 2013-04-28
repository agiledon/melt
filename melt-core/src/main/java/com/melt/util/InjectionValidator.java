package com.melt.util;

import com.melt.config.BeanInfo;
import com.melt.exceptions.BeanConfigurationException;

public class InjectionValidator {
    public static <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            throw new BeanConfigurationException(String.format("%s can't be interface type.", propertyClass.getName()));
        }
    }

    public static void validateBeanIsRegistered(BeanInfo currentBean) {
        if (currentBean == null) {
            throw new BeanConfigurationException("Didn't register main bean");
        }
    }
}
