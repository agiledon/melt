package com.melt.config.autowired;

import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoWiredByName implements AutoWired {
    public void autoWired(BeansContainer beansContainer, BeanInfo beanInfo) {
        Field[] fields =  beanInfo.getClazz().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
//        for (Method method : beanInfo.getClazz().getDeclaredMethods()) {
//            if (method.getName().startsWith("set")) {
//                method
//                if (parameterTypes.length == 0) {
//                    Class parameterType = parameterTypes[0];
//                }
//            }
//        }
    }
}
