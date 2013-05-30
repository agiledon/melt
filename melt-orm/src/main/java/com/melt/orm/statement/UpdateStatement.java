package com.melt.orm.statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.ObjectArrays.newArray;
import static com.melt.orm.criteria.By.nil;
import static com.melt.orm.util.FieldValueWrapper.wrap;

public class UpdateStatement extends NonQueryStatement {
    private final static Joiner fieldsJoiner = Joiner.on(", ");

    public UpdateStatement(Session session) {
        super(session);
    }

    public <T> SqlStatement assemble(T targetEntity, Criteria criteria) {
        ModelConfig modelConfig = session.getModelConfig(targetEntity.getClass());

        sqlBuilder.append("UPDATE ");
        sqlBuilder.append(modelConfig.getTableName());
        assembleSettingClause(targetEntity, modelConfig);
        assembleConditionClause(criteria);

        return this;
    }

    public <T> SqlStatement assemble(T targetEntity) {
        return assemble(targetEntity, nil());
    }

    private <T> void assembleSettingClause(T targetEntity, ModelConfig modelConfig) {
        sqlBuilder.append(buildSettingClause(targetEntity, modelConfig));
    }

    private <T> StringBuilder buildSettingClause(final T targetEntity, ModelConfig modelConfig) {
        StringBuilder settingClauseBuilder = new StringBuilder();
        settingClauseBuilder.append(" SET ");

        fieldsJoiner.skipNulls().appendTo(settingClauseBuilder,
                from(modelConfig.getFields()).transform(new Function<FieldConfig, Object>() {
            @Override
            public Object apply(FieldConfig fieldConfig) {
                if (fieldConfig.isPrimaryKeyField() || fieldConfig.isNeedBeProxy()) {
                    return null;
                }
                return concatSettingValues(fieldConfig, targetEntity);
            }
        }));
        return settingClauseBuilder;
    }

    private <T> String concatSettingValues(FieldConfig fieldConfig, T targetEntity) {
        return fieldConfig.getColumnName() + " = " + wrap(fieldConfig.getFieldValue(targetEntity));
    }

}
