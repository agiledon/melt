package com.melt.orm.mapping;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.Map;

public class ProxyFactory {
    public static  <T> T getProxy(Class modelClass, Map<String, ModelConfig> modelConfigs, Session session) {
        Enhancer en = new Enhancer();
        en.setSuperclass(modelClass);
        Callback authProxy = new ProxyCallback(session);
        en.setCallback(authProxy);
        return (T)en.create();
    }
}
