package com.melt.orm.mapping;

import com.melt.orm.session.Session;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

public class ProxyFactory {
    public static  <T> T getProxy(Class modelClass, Session session) {
        Enhancer en = new Enhancer();
        en.setSuperclass(modelClass);
        Callback authProxy = new ProxyCallback(session, modelClass);
        en.setCallback(authProxy);
        return (T)en.create();
    }
}
