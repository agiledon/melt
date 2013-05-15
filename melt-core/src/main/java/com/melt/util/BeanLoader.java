package com.melt.util;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanLoader {
    private static Logger logger = LoggerFactory.getLogger(BeanLoader.class);

    public static Object loadReferenceBean(InitializedBeans initializedBeans,
                                           Container parentContainer,
                                           String refName) {
        Object bean = initializedBeans.getBean(refName);
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(refName);
        }
        if (bean == null) {
            String message = String.format("Can't get object of RefBean with Ref %s", refName);
            logger.error(message);
            throw new InitBeanException(message);
        }
        return bean;
    }
}
