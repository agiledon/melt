package com.melt.orm.mapping;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

public class ProxyCallback implements MethodInterceptor {
    private Map<String, ModelConfig> modelConfigs;
    private Session session;

    public ProxyCallback(Map<String, ModelConfig> modelConfigs, Session session) {
        this.modelConfigs = modelConfigs;
        this.session = session;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();
        ModelConfig modelConfig = modelConfigs.get(obj.getClass().getName());
        FieldConfig fieldConfig = modelConfig.getFieldConfigByMethodName(methodName);
        if (fieldConfig.isNeedBeProxy() && methodName.startsWith("get")) {
            if (fieldConfig.isOneToManyField()) {
                //Do one to many
                String fieldClassName = fieldConfig.getGenericType().getName();
                ModelConfig fieldModelConfig = modelConfigs.get(fieldClassName);

            }
            return session.find();
        } else {
            return proxy.invokeSuper(obj, args);
        }
    }
}
