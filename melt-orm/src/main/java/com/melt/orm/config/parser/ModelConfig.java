package com.melt.orm.config.parser;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.melt.orm.dialect.DatabaseDialect;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;

public class ModelConfig {
    private final static Joiner uppercaseJoiner = Joiner.on("_");
    private List<FieldConfig> fields;
    private Class modelClass;

    public ModelConfig(List<FieldConfig> fields, Class modelClass) {
        this.fields = fields;
        this.modelClass = modelClass;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public List<FieldConfig> getPrimaryKeys() {
        return from(fields).filter(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(com.melt.orm.config.parser.FieldConfig fieldConfig) {
                return fieldConfig.isPrimaryKeyField();
            }
        }).toList();
    }

    public String getTableName() {
        String simpleName = modelClass.getSimpleName();
        return splitWordsByUpperCaseChar(simpleName) + "S";
    }

    public List<FieldConfig> getFields() {
        return fields;
    }

    public String generateCreateTableSQL(DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(getTableName());
        sb.append("(\n");
        List<FieldConfig> primaryKeys = getPrimaryKeys();
        for (FieldConfig field : fields) {
            if (dialect.isBasicType(field.getFieldType())) {
                sb.append(splitWordsByUpperCaseChar(field.getFieldName()));
                sb.append(" ");
                sb.append(dialect.mappingFieldType(field.getFieldType()));
                sb.append(" ");
            } else if (isOneToOne(modelConfigs, field) || isManyToOne(modelConfigs, field)) {
                sb.append(splitWordsByUpperCaseChar(field.getFieldName()));
                sb.append("_ID");
                sb.append(" ");
//                sb.append()
            } else {
                continue;
            }
            sb.append("\n");
        }
        sb.append(")");
        return sb.toString();
    }

    private boolean isOneToOne(Map<String, ModelConfig> modelConfigs, FieldConfig field) {
        String fieldTypeName = field.getFieldType().getName();
        if (!modelConfigs.containsKey(fieldTypeName)) {
            return false;
        }
        ModelConfig modelConfig = modelConfigs.get(fieldTypeName);
        return modelConfig.hasFieldWithType(getModelClass());
    }

    private boolean isManyToOne(Map<String, ModelConfig> modelConfigs, FieldConfig field) {
        String fieldTypeName = field.getFieldType().getName();
        if (!modelConfigs.containsKey(fieldTypeName)) {
            return false;
        }
        ModelConfig modelConfig = modelConfigs.get(fieldTypeName);
        return modelConfig.hasSetFieldWithType(getModelClass());
    }

    public boolean hasFieldWithType(final Class modelClass) {
        return from(fields).anyMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(com.melt.orm.config.parser.FieldConfig fieldConfig) {
                return fieldConfig.getFieldType().getName().equals(modelClass.getName());
            }
        });
    }

    public boolean hasSetFieldWithType(final Class modelClass) {
        return from(fields).anyMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(com.melt.orm.config.parser.FieldConfig fieldConfig) {
                return fieldConfig.isSetType() && fieldConfig.getGenericType().getName().equals(modelClass.getName());
            }
        });
    }

    private String splitWordsByUpperCaseChar(String words) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, words);
    }

    private boolean isMoreThanOnePrimaryKey(List<FieldConfig> primaryKeys) {
        return primaryKeys.size() > 1;
    }
}
