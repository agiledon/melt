package com.melt.core;

import com.melt.config.Configuration;

public class Context {
    private Configuration configuration;

    public Context(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T resolve() {
        return null;
    }
}
