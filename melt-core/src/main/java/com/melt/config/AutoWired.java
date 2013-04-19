package com.melt.config;

import com.melt.core.injector.Injector;

public enum AutoWired {
    NAME {
        @Override
        public Injector injector() {
            return null;
        }
    }, TYPE {
        @Override
        public Injector injector() {
            return null;
        }
    };
    public abstract Injector injector();
}
