package com.melt.orm.mapping;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

import static com.melt.orm.criteria.By.eq;

public class ProxyCallback implements MethodInterceptor {
    private Map<String, ModelConfig> modelConfigs;
    private Session session;

    public ProxyCallback(Session session) {
        this.modelConfigs = session.getModelConfigs();
        this.session = session;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();
        ModelConfig modelConfig = modelConfigs.get(obj.getClass().getName());
        FieldConfig fieldConfig = modelConfig.getFieldConfigByMethodName(methodName);
        if (fieldConfig.isNeedBeProxy() && methodName.startsWith("get")) {
            if (fieldConfig.isOneToManyField()) {
                String fieldClassName = fieldConfig.getGenericType().getName();
                ModelConfig fieldModelConfig = modelConfigs.get(fieldClassName);

//                session.find(fieldModelConfig.getModelClass(), eq("", ));
            }
            return session.find(null, null);
        } else {
            return proxy.invokeSuper(obj, args);
        }
    }
}
