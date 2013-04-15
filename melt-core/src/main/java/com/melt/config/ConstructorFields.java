package com.melt.config;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ConstructorFields {
    private List<ConstructorField> fields = newArrayList();

    public ConstructorFields(List<ConstructorField> fields) {
        this.fields = fields;
    }

    public List<ConstructorField> getFields() {
        return fields;
    }
}
