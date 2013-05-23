package com.melt.orm.config.parser;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.melt.orm.dialect.DatabaseDialect;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;

public class ModelConfig {
    private final static Joiner uppercaseJoiner = Joiner.on("_");
    private final Joiner joiner = Joiner.on(",\n");
    private List<FieldConfig> fields;
    private Class modelClass;

    public ModelConfig(List<FieldConfig> fields, Class modelClass) {
        this.fields = fields;
        this.modelClass = modelClass;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public Optional<FieldConfig> getPrimaryKey() {
        return from(fields).filter(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig fieldConfig) {
                return fieldConfig.isPrimaryKeyField();
            }
        }).first();
    }

    public String getTableName() {
        String simpleName = modelClass.getSimpleName();
        return splitWordsByUpperCaseChar(simpleName) + "S";
    }

    public List<FieldConfig> getFields() {
        return fields;
    }

    public String generateDropTableSQL(){
        final StringBuilder sb = new StringBuilder("DROP TABLE ");
        sb.append(getTableName());
        return sb.toString();
    }

    public String generateCreateTableSQL(final DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs) {
        final StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(getTableName());
        sb.append("(\n");
        sb.append(joiner
                .skipNulls()
                .join(getFieldSQLs(dialect, modelConfigs)));
        sb.append(")");
        return sb.toString();
    }

    private FluentIterable<String> getFieldSQLs(final DatabaseDialect dialect, final Map<String, ModelConfig> modelConfigs) {
        return from(fields).transform(new Function<FieldConfig, String>() {
            @Override
            public String apply(FieldConfig field) {
                StringBuilder fieldSb = new StringBuilder();
                if (dialect.isBasicType(field.getFieldType())) {
                    fieldSb.append(splitWordsByUpperCaseChar(field.getFieldName()));
                    fieldSb.append(" ");
                    fieldSb.append(dialect.mappingFieldType(field.getFieldType()));
                    fieldSb.append(" ");
                    if (field.isPrimaryKeyField()) {
                        fieldSb.append(dialect.getAutoIncreaseColumn());
                    }
                } else if (field.isManyToOneField() || field.isOneToOneField()) {
                    fieldSb.append(splitWordsByUpperCaseChar(field.getFieldName()));
                    fieldSb.append("_ID");
                    fieldSb.append(" ");
                    ModelConfig referenceModelConfig = modelConfigs.get(field.getFieldType().getName());
                    Optional<FieldConfig> primaryKey = referenceModelConfig.getPrimaryKey();
                    if (primaryKey.isPresent()) {
                        fieldSb.append(dialect.mappingFieldType(primaryKey.get().getFieldType()));
                    }else {
                        fieldSb.append(dialect.mappingFieldType(Integer.TYPE));
                    }
                    fieldSb.append(" ");
                    fieldSb.append("NOT NULL");
                } else {
                    return null;
                }
                return fieldSb.toString();
            }
        });
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
}
