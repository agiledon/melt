package com.melt.config;

import com.melt.config.autowired.AutoWired;

public enum AutoWiredBy {
    NAME {
        @Override
        public AutoWired injector() {
            return null;
        }

        @Override
        public boolean isNotNull() {
            return true;
        }

        @Override
        public boolean isByName() {
            return true;
        }
    }, TYPE {
        @Override
        public AutoWired injector() {
            return null;
        }

        @Override
        public boolean isNotNull() {
            return true;
        }

        @Override
        public boolean isByName() {
            return false;
        }
    },
    NULL {
        @Override
        public AutoWired injector() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNotNull() {
            return false;
        }

        @Override
        public boolean isByName() {
            return false;
        }
    };

    public abstract AutoWired injector();

    public abstract boolean isNotNull();

    public abstract boolean isByName();
}
