package com.melt.core;

import com.melt.config.Configs;

public class Container {
    private Configs configs;

    public Container(Configs configs) {
        this.configs = configs;
    }

    public <T> T resolve() {
        return null;
    }
}
