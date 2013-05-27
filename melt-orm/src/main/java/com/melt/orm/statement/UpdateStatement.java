package com.melt.orm.statement;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;
import com.melt.orm.util.FieldValueWrapper;

import java.lang.reflect.InvocationTargetException;

public class UpdateStatement extends NonQueryStatement {
    public UpdateStatement(Session session) {
        super(session);
    }

    public <T> SqlStatement assemble(T targetEntity, Criteria criteria) {
        ModelConfig modelConfig = getModelConfig(targetEntity.getClass());

        sqlBuilder.append("UPDATE ");
        sqlBuilder.append(modelConfig.getTableName());
        assembleSettingClause(targetEntity, modelConfig);
        assembleConditionClause(criteria);

        return this;
    }

    private <T> void assembleSettingClause(T targetEntity, ModelConfig modelConfig) {
        sqlBuilder.append(buildSettingClause(targetEntity, modelConfig));
    }

    private <T> StringBuilder buildSettingClause(T targetEntity, ModelConfig modelConfig) {
        StringBuilder settingClauseBuilder = new StringBuilder();
        settingClauseBuilder.append(" SET ");
        for (FieldConfig field : modelConfig.getFields()) {
            if (field.isPrimaryKeyField() || field.isNeedBeProxy()) {
                continue;
            }
            if (isNotFirstField(settingClauseBuilder)) {
                settingClauseBuilder.append(", ");
            }
            buildSettingFieldValue(targetEntity, settingClauseBuilder, field);
        }
        return settingClauseBuilder;
    }

    private <T> void buildSettingFieldValue(T targetEntity, StringBuilder settingClauseBuilder, FieldConfig field) {
        settingClauseBuilder.append(field.getColumnName());
        settingClauseBuilder.append(" = ");
        settingClauseBuilder.append(FieldValueWrapper.wrap(getFieldValue(targetEntity, field)));
    }

    private boolean isNotFirstField(StringBuilder settingClauseBuilder) {
        return !settingClauseBuilder.toString().equals(" SET ");
    }

    private <T> Object getFieldValue(T targetEntity, FieldConfig field) {
        Object fieldValue;
        try {
            fieldValue = field.getReader().invoke(targetEntity, null);
        } catch (IllegalAccessException e) {
            fieldValue = null;
        } catch (InvocationTargetException e) {
            fieldValue = null;
        }
        return fieldValue;
    }
}
