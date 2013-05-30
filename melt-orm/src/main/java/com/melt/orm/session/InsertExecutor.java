package com.melt.orm.session;

import com.google.common.collect.Maps;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.By;
import com.melt.orm.statement.InsertStatement;
import com.melt.orm.statement.UpdateStatement;

import java.util.Collection;
import java.util.Map;

public class InsertExecutor {
    private final Map<String,Integer> foreignKeys = Maps.newHashMap();
    private Session session;

    public InsertExecutor(Session session) {
        this.session = session;
    }

    public <T> int execute(T targetEntity) {
        ModelConfig modelConfig = session.getModelConfig(targetEntity.getClass());

        insertParentEntity(targetEntity, modelConfig);
        insertAllChildEntities(targetEntity, modelConfig);

        int primaryKey = insertCurrentEntity(targetEntity);

        insertAllChildrenEntities(targetEntity, modelConfig, primaryKey);
        updateAllChildEntity(targetEntity, modelConfig, primaryKey);

        return primaryKey;
    }

    private <T> void updateAllChildEntity(T targetEntity, ModelConfig modelConfig, int primaryKey) {
        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isOneToOneField()) {
                updateChildEntity(targetEntity, primaryKey, fieldConfig);
            }
        }
    }

    private <T> void updateChildEntity(T targetEntity, int primaryKey, FieldConfig fieldConfig) {
        Object fieldValue = fieldConfig.getFieldValue(targetEntity);
        if (fieldValue != null) {
            ModelConfig subModelConfig = session.getModelConfig(fieldValue.getClass());
            for (FieldConfig subFieldConfig : subModelConfig.getFields()) {
                if (subFieldConfig.isOneToOneField()) {
                    UpdateStatement updateStatement = new UpdateStatement(session);
                    updateStatement.assemble(fieldValue, By.eq(subFieldConfig.getOriginReferenceColumnName(), -1));
                    updateStatement.setForeignKey(subFieldConfig.getReferenceColumnName(), primaryKey);
                    updateStatement.createNonQueryCommand().execute();
                }
            }
        }
    }

    private <T> void insertAllChildrenEntities(T targetEntity, ModelConfig modelConfig, int primaryKey) {
        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isOneToManyField()) {
                insertChildrenEntities(targetEntity, primaryKey, fieldConfig);
            }
        }
    }

    private <T> void insertChildrenEntities(T targetEntity, int primaryKey, FieldConfig fieldConfig) {
        Class genericType = fieldConfig.getGenericType();
        Collection collection = (Collection) fieldConfig.getFieldValue(targetEntity);
        if (collection != null) {
            for (Object child : collection) {
                insertEachChildEntity(primaryKey, genericType, child);
            }
        }
    }

    private void insertEachChildEntity(int primaryKey, Class genericType, Object child) {
        InsertStatement statement = new InsertStatement(session);
        statement.assemble(child);
        statement.assembleManyToOne(primaryKey, genericType);
        statement.createNonQueryCommand().execute();
    }

    private <T> int insertCurrentEntity(T targetEntity) {
        InsertStatement statement = new InsertStatement(session);
        statement.assemble(targetEntity);
        for (Map.Entry<String, Integer> foreignKey : foreignKeys.entrySet()) {
            statement.setForeignKey(foreignKey.getKey(), foreignKey.getValue());
        }

        return statement.createNonQueryCommand().execute();
    }

    private <T> void insertAllChildEntities(T targetEntity, ModelConfig modelConfig) {
        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isOneToOneField()) {
                insertChildEntity(targetEntity, fieldConfig);
            }
        }
    }

    private <T> void insertChildEntity(T targetEntity, FieldConfig fieldConfig) {
        Object fieldValue = fieldConfig.getFieldValue(targetEntity);
        if (fieldValue != null) {
            InsertStatement statement = new InsertStatement(session);
            statement.assemble(fieldValue);
            statement.assembleOneToOne(fieldValue);
            int foreignKey = statement.createNonQueryCommand().execute();
            foreignKeys.put(fieldConfig.getReferenceColumnName(), foreignKey);
        }
    }

    private <T> void insertParentEntity(T targetEntity, ModelConfig modelConfig) {
        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isManyToOneField()) {
                insertFieldValue(targetEntity, fieldConfig);
            }
        }
    }

    private <T> void insertFieldValue(T targetEntity, FieldConfig fieldConfig) {
        Object fieldValue = fieldConfig.getFieldValue(targetEntity);
        if (fieldValue != null) {
            InsertStatement statement = new InsertStatement(session);
            statement.assemble(fieldValue);
            int foreignKey = statement.createNonQueryCommand().execute();
            foreignKeys.put(fieldConfig.getReferenceColumnName(), foreignKey);
        }
    }
}