package com.github.agiledon.melt.config.autowired;

public enum AutoWiredBy {
    NAME {
        @Override
        public AutoWired autoWired() {
            return new AutoWiredByName();
        }
    }, TYPE {
        @Override
        public AutoWired autoWired() {
            return new AutoWiredByType();
        }
    },
    NULL {
        @Override
        public AutoWired autoWired() {
            return new AutoWiredByNull();
        }
    };

    public abstract AutoWired autoWired();
}
