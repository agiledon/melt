package com.melt.orm.statement;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

import java.lang.reflect.InvocationTargetException;

public class UpdateStatement extends NonQueryStatement {
    public UpdateStatement(Session session) {
        super(session);
    }

    public <T> SqlStatement assemble(T targetEntity, Criteria criteria) {
        ModelConfig modelConfig = getModelConfig(targetEntity.getClass());

        sqlBuilder.append("UPDATE ");
        sqlBuilder.append(modelConfig.getTableName());
        sqlBuilder.append(" SET ");
        for (FieldConfig field : modelConfig.getFields()) {
            if (field.isPrimaryKeyField() || field.isNeedBeProxy()) {
                break;
            }
            Object fieldValue = getFieldValue(targetEntity, field);
            sqlBuilder.append(field.getColumnName());
            sqlBuilder.append(" = ");
        }

        assembleConditionClause(criteria);

        return this;
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
