package com.melt.config.constructor;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ConstructorParameters {
    private List<ConstructorParameter> parameters = newArrayList();

    public ConstructorParameters(List<ConstructorParameter> parameters) {
        this.parameters = parameters;
    }

    public List<ConstructorParameter> getParameters() {
        return parameters;
    }
}
