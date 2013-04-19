package com.melt.config;

import com.melt.core.injector.Injector;

public enum AutoWired {
    NAME {
        @Override
        public Injector injector() {
            return null;
        }

        @Override
        public boolean isNull() {
            return false;
        }
    }, TYPE {
        @Override
        public Injector injector() {
            return null;
        }

        @Override
        public boolean isNull() {
            return false;
        }
    },
    NULL {
        @Override
        public Injector injector() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNull() {
            return true;
        }
    };

    public abstract Injector injector();

    public abstract boolean isNull();
}
