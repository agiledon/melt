package com.github.agiledon.melt.util;

import com.github.agiledon.melt.core.InjectionContext;
import com.github.agiledon.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanLoader {
    private static Logger logger = LoggerFactory.getLogger(BeanLoader.class);

    public static Object loadReferenceBean(InjectionContext injectionContext, String refName) {
        Object bean = injectionContext.getInitializedBeans().getBean(refName);
        if (bean == null && injectionContext.getParentContainer() != null) {
            bean = injectionContext.getParentContainer().resolve(refName);
        }
        if (bean == null) {
            String message = String.format("Can't get object of RefBean with Ref %s", refName);
            logger.error(message);
            throw new InitBeanException(message);
        }
        return bean;
    }
}
