package com.melt.orm.session;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.By;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.statement.UpdateStatement;
import com.melt.orm.util.GlobalConsent;

public class UpdateExecutor extends CommandExecutor {

    public UpdateExecutor(Session session) {
        super(session);
    }

    public <T> int execute(T targetEntity, Criteria criteria) {
        UpdateStatement statement = new UpdateStatement(session);
        statement.assemble(targetEntity, criteria);
        int primaryKey = getId(targetEntity);

        ModelConfig modelConfig = session.getModelConfig(targetEntity.getClass());
        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isOneToOneField()) {
                updateAllChildEntities(targetEntity, statement, primaryKey, fieldConfig);
            }
        }

        return statement.createNonQueryCommand().execute();
    }

    private <T> void updateAllChildEntities(T targetEntity, UpdateStatement statement, int primaryKey, FieldConfig fieldConfig) {
        Object fieldValue = fieldConfig.getFieldValue(targetEntity);
        int foreignKey = getId(fieldValue);
        statement.setForeignKey(fieldConfig.getReferenceColumnName(), foreignKey);

        ModelConfig subModelConfig = session.getModelConfig(fieldValue.getClass());
        for (FieldConfig subFieldConfig : subModelConfig.getFields()) {
            if (subFieldConfig.isOneToOneField()) {
                updateChileEntity(primaryKey, fieldValue, subFieldConfig);
            }
        }
    }

    private void updateChileEntity(int primaryKey, Object fieldValue, FieldConfig subFieldConfig) {
        UpdateStatement innerStatement = new UpdateStatement(session);
        innerStatement.assemble(fieldValue, By.eq(subFieldConfig.getOriginReferenceColumnName(), primaryKey));
        innerStatement.setForeignKey(subFieldConfig.getReferenceColumnName(), primaryKey);
        innerStatement.createNonQueryCommand().execute();
    }

}