package com.melt.util;

import java.lang.reflect.Constructor;

public class ReflectionUtil {
    public ReflectionUtil() {
    }

    public static <T> Constructor<T> findConstructor(Class<T> klass, Class<T>[] parameterTypes) throws NoSuchMethodException {
        @SuppressWarnings("unchecked")
        Constructor<T>[] constructors = (Constructor<T>[]) klass
                .getConstructors();
        for (Constructor<T> constructor : constructors) {
            Class<?>[] thisConstructorParameterTypes = constructor
                    .getParameterTypes();
            if (thisConstructorParameterTypes.length == parameterTypes.length) {
                boolean match = true;
                match = matchingParameterType(parameterTypes, thisConstructorParameterTypes, match);
                if (match) {
                    return constructor;
                }
            }
        }
        throw new NoSuchMethodException();
    }

    private static <T> boolean matchingParameterType(Class<T>[] parameterTypes, Class<?>[] thisConstructorParameterTypes, boolean match) {
        for (int i = 0; i < thisConstructorParameterTypes.length
                && match; i++) {
            if (!thisConstructorParameterTypes[i]
                    .isAssignableFrom(parameterTypes[i]))
                match = false;
        }
        return match;
    }
}