package com.melt.core.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ConstructorInjector {
    public <T> T inject(Class<T> targetClass, Object... dependencies) {
        try {
            Class[] classes = getClassesFrom(dependencies);
            Constructor<T> constructor = targetClass.getConstructor(classes);
            return constructor.newInstance(dependencies);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class[] getClassesFrom(Object[] dependencies) {
        List<Class> classes = newArrayList();
        for (Object dependency : dependencies) {
            classes.add(dependency.getClass());
        }
        return classes.toArray(new Class[classes.size()]);
    }

}
