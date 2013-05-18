package com.melt.orm.config.parser;

import java.util.List;

public class ModelConfig {
    private List<FieldConfig> fields;
    private Class modelClass;

    public ModelConfig(List<FieldConfig> fields, Class modelClass) {
        this.fields = fields;
        this.modelClass = modelClass;
    }

}
