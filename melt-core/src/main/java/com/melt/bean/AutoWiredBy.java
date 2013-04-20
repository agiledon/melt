package com.melt.bean;

import com.melt.bean.autowired.AutoWired;
import com.melt.bean.autowired.AutoWiredByName;
import com.melt.bean.autowired.AutoWiredByNull;
import com.melt.bean.autowired.AutoWiredByType;

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
