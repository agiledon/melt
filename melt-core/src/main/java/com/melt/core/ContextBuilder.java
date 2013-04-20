package com.melt.core;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.property.*;
import com.melt.core.initializer.ContainerInitializer;
import com.melt.exceptions.BeanConfigurationException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ContextBuilder {
    private final ContainerInitializer containerInitializer = new ContainerInitializer();
    private List<BeanInfo> beans = newArrayList();
    private final Context context = new Context();
    private BeanInfo currentBean = null;
    private Context parentContext;

    public Context build() {
        BeansContainer beansContainer = containerInitializer.initialize(beans);
        context.setContainer(beansContainer);
        context.setParentContext(parentContext);
        return context;
    }

    public <T> ContextBuilder register(Class<T> registeredClass) {
        BeanInfo registeredBean = new BeanInfo(getBeanName(registeredClass), registeredClass);
        beans.add(registeredBean);
        currentBean = registeredBean;
        ConstructorIndexer.reset();
        return this;
    }

    public <T> ContextBuilder construct(Class<T> constructorParameterClass) {
        ConstructorParameter constructorParameter = new ConstructorParameter(
                ConstructorIndexer.index(), constructorParameterClass.getSimpleName());
        if (currentBean != null) {
            currentBean.addConstructorParameter(constructorParameter);
            registerBeanInfoWithClass(constructorParameterClass);
        } else {
            throw new BeanConfigurationException("Didn't register main bean");
        }
        return this;
    }

    public <T> ContextBuilder parent(Context context) {
        this.parentContext = context;
        return this;
    }

    public <T> ContextBuilder withClass(Class<T> propertyClass) {
        addProperty(propertyClass);
        registerBeanInfoWithClass(propertyClass);
        ConstructorIndexer.reset();
        return this;
    }

    public ContextBuilder withValue(String propertyName, int propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanIntProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContextBuilder withValue(String propertyName, double propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanDoubleProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContextBuilder withValue(String propertyName, float propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanFloatProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContextBuilder withValue(String propertyName, long propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanLongProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    private void addPropertyAndResetConstructorIndexer(BeanProperty property) {
        currentBean.addProperty(property);
        ConstructorIndexer.reset();
    }

    public ContextBuilder withValue(String propertyName, String propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanStringProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    public ContextBuilder withValue(String propertyName, Object propertyValue) {
        addPropertyAndResetConstructorIndexer(new BeanObjectProperty(currentBean, propertyName, propertyValue));
        return this;
    }

    private <T> String getBeanName(Class<T> registeredClass) {
        Class<?>[] interfaces = registeredClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                if (matchInterfaceClassName(registeredClass, anInterface)) {
                    return anInterface.getSimpleName();
                }
            }
        }
        return registeredClass.getSimpleName();
    }

    private <T> void addProperty(Class<T> propertyClass) {
        BeanProperty property = new BeanRefProperty(
                currentBean,
                getBeanName(propertyClass),
                getBeanName(propertyClass));
        currentBean.addProperty(property);

    }

    private <T> boolean matchInterfaceClassName(Class<T> registeredClass, Class<?> anInterface) {
        return registeredClass.getSimpleName().toLowerCase().contains(anInterface.getSimpleName().toLowerCase());
    }

    private <T> boolean registerBeanInfoWithClass(Class<T> propertyClass) {
        return beans.add(new BeanInfo(getBeanName(propertyClass), propertyClass));
    }

    private static class ConstructorIndexer {
        private static int index = 0;

        public static int index() {
            return index++;
        }

        public static void reset() {
            index = 0;
        }
    }

}
