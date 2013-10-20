package com.github.agiledon.melt.util;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.exceptions.BeanConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjectionValidator {
    private static Logger logger = LoggerFactory.getLogger(InjectionValidator.class);

    public static <T> void validateIsInterface(Class<T> propertyClass) {
        if (propertyClass.isInterface()) {
            String message = String.format("%s can't be interface type.", propertyClass.getName());
            logger.error(message);
            throw new BeanConfigurationException(message);
        }
    }

    public static void validateBeanIsRegistered(BeanInfo currentBean) {
        if (currentBean == null) {
            String message = "Didn't register main bean";
            logger.error(message);
            throw new BeanConfigurationException(message);
        }
    }
}
