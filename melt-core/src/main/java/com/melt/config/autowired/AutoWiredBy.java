package com.melt.config.autowired;

import com.melt.config.autowired.AutoWired;
import com.melt.config.autowired.AutoWiredByName;
import com.melt.config.autowired.AutoWiredByNull;
import com.melt.config.autowired.AutoWiredByType;

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
