package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.InitBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefConstructorParameter extends ConstructorParameter {
    private String ref;
    private Logger logger = LoggerFactory.getLogger(RefConstructorParameter.class);

    public RefConstructorParameter(int index, String ref) {
        super(index);
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public void updateValue(Container parentContainer, InitializedBeans initializedBeans) {
        Object bean = initializedBeans.getBean(getRef());
        if (bean == null && parentContainer != null) {
            bean = parentContainer.resolve(getRef());
        }
        if (bean == null) {
            String message = String.format("Can't get object of RefConstructorParameter with Ref %s", ref);
            logger.error(message);
            throw new InitBeanException(message);
        }
        setValue(bean);
    }
}
