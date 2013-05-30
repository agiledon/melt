package com.melt.orm.config.parser;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.util.NameMapping;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.util.NameMapping.getMappedName;
import static java.beans.Introspector.getBeanInfo;

public class ModelConfig {
    private final Joiner joiner = Joiner.on(",\n");
    private List<FieldConfig> fields;
    private Class modelClass;
    private Map<String, FieldConfig> fieldNameAndFieldConfigMap;
    private Map<String, FieldConfig> methodAndFieldConfigMap;
    private String referenceColumnName;

    public ModelConfig(List<FieldConfig> fields, Class modelClass) {
        this.fields = fields;
        this.modelClass = modelClass;
        initFieldNameAndFiledConfigMap();
        initMethodAndFieldConfigMap();
        this.referenceColumnName = getModelClass().getSimpleName() + "Id";
    }

    private void initFieldNameAndFiledConfigMap() {
        fieldNameAndFieldConfigMap = newHashMap();
        for (FieldConfig field : fields) {
            fieldNameAndFieldConfigMap.put(field.getFieldName(), field);
        }
    }

    private void initMethodAndFieldConfigMap() {
        methodAndFieldConfigMap = newHashMap();
        try {
            BeanInfo beanInfo = getBeanInfo(modelClass);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String name = propertyDescriptor.getName();
                if (!name.equals("class")) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Method writeMethod = propertyDescriptor.getWriteMethod();

                    FieldConfig fieldConfig = fieldNameAndFieldConfigMap.get(name);
                    fieldConfig.setReader(readMethod);
                    fieldConfig.setWriter(writeMethod);

                    methodAndFieldConfigMap.put(readMethod.getName(), fieldConfig);
                    methodAndFieldConfigMap.put(writeMethod.getName(), fieldConfig);
                }
            }
        } catch (IntrospectionException e) {
            throw new MeltOrmException("Not valid JavaBean");
        }
    }

    public FieldConfig getFieldConfigByMethodName(String methodName) {
        return methodAndFieldConfigMap.get(methodName);
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

    public boolean isNeedBeProxy() {
        return from(fields).anyMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig fieldConfig) {
                return fieldConfig.isNeedBeProxy();
            }
        });
    }

    public String getTableName() {
        String simpleName = modelClass.getSimpleName();
        return NameMapping.getMappedName(simpleName) + "S";
    }

    public List<FieldConfig> getFields() {
        return fields;
    }

    public String generateDropTableSQL() {
        final StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ");
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
        sb.append("\n)");
        return sb.toString();
    }

    private FluentIterable<String> getFieldSQLs(final DatabaseDialect dialect, final Map<String, ModelConfig> modelConfigs) {
        return from(fields).transform(new Function<FieldConfig, String>() {
            @Override
            public String apply(FieldConfig field) {
                StringBuilder fieldSb = new StringBuilder();
                fieldSb.append("    ");
                if (dialect.isBasicType(field.getFieldType())) {
                    fieldSb.append(NameMapping.getMappedName(field.getFieldName()));
                    fieldSb.append(" ");
                    fieldSb.append(dialect.mappingFieldType(field.getFieldType()));
                    fieldSb.append(" ");
                    if (field.isPrimaryKeyField()) {
                        fieldSb.append(dialect.getAutoIncreaseColumn());
                    }
                } else if (field.isManyToOneField() || field.isOneToOneField()) {
                    fieldSb.append(field.getReferenceColumnName());
                    fieldSb.append(" ");
                    ModelConfig referenceModelConfig = modelConfigs.get(field.getFieldType().getName());
                    Optional<FieldConfig> primaryKey = referenceModelConfig.getPrimaryKey();
                    if (primaryKey.isPresent()) {
                        fieldSb.append(dialect.mappingFieldType(primaryKey.get().getFieldType()));
                    } else {
                        fieldSb.append(dialect.mappingFieldType(Integer.TYPE));
                    }
                    fieldSb.append(" ");
                } else if (field.isEnum()) {
                    fieldSb.append(NameMapping.getMappedName(field.getFieldName()));
                    fieldSb.append(" ");
                    fieldSb.append(dialect.mappingFieldType(String.class));
                    fieldSb.append(" ");
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
            public boolean apply(FieldConfig fieldConfig) {
                return fieldConfig.getFieldType().getName().equals(modelClass.getName());
            }
        });
    }

    public boolean hasSetFieldWithType(final Class modelClass) {
        return from(fields).anyMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig fieldConfig) {
                return fieldConfig.isSetType() && fieldConfig.getGenericType().getName().equals(modelClass.getName());
            }
        });
    }

    public String getReferenceColumnName() {
        return referenceColumnName;
    }

    public FieldConfig getFieldConfigByFieldName(String fieldName) {
        return fieldNameAndFieldConfigMap.get(fieldName);
    }
}
