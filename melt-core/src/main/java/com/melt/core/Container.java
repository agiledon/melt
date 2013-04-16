package com.melt.core;

import com.melt.config.Configuration;

public class Container {
    private Configuration configuration;

    public Container(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T resolve() {
        return null;
    }
}
