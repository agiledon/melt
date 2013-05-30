package com.melt.orm.statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.melt.orm.command.InsertCommand;
import com.melt.orm.command.NonQueryCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import com.melt.orm.util.FieldValueWrapper;

import static com.google.common.collect.FluentIterable.from;

public class InsertStatement extends NonQueryStatement {
    private final static Joiner fieldsJoiner = Joiner.on(", ");

    public InsertStatement(Session session) {
        super(session);
    }

    public <T> SqlStatement assemble(T targetEntity) {
        ModelConfig modelConfig = session.getModelConfig(targetEntity.getClass());

        sqlBuilder.append("INSERT INTO ");
        sqlBuilder.append(modelConfig.getTableName());
        sqlBuilder.append(" ");
        assembleValuesClause(targetEntity, modelConfig);

        return this;
    }

    @Override
    public NonQueryCommand createNonQueryCommand() {
        return new InsertCommand(session.getConnection(), this);
    }

    private <T> void assembleValuesClause(final T targetEntity, ModelConfig modelConfig) {
        sqlBuilder.append(buildFieldNameClause(modelConfig));
        sqlBuilder.append(" VALUES ");
        sqlBuilder.append(buildFieldValueClause(targetEntity, modelConfig));
    }

    private StringBuilder buildFieldNameClause(ModelConfig modelConfig) {
        return buildFieldClause(
                from(modelConfig.getFields()).transform(new Function<FieldConfig, Object>() {
                    @Override
                    public Object apply(FieldConfig fieldConfig) {
                        if (fieldConfig.isPrimaryKeyField() || fieldConfig.isOneToManyField()) {
                            return null;
                        }
                        if (fieldConfig.isManyToOneField() || fieldConfig.isOneToOneField()) {
                            return fieldConfig.getReferenceColumnName();
                        }
                        return fieldConfig.getColumnName();
                    }
                }));
    }

    private <T> StringBuilder buildFieldValueClause(final T targetEntity, ModelConfig modelConfig) {
        return buildFieldClause(
                from(modelConfig.getFields()).transform(new Function<FieldConfig, Object>() {
                    @Override
                    public Object apply(FieldConfig fieldConfig) {
                        if (fieldConfig.isPrimaryKeyField() || fieldConfig.isOneToManyField()) {
                            return null;
                        }
                        if (fieldConfig.isManyToOneField() || fieldConfig.isOneToOneField()) {
                            return String.format("${%s}", fieldConfig.getReferenceColumnName());
                        }
                        return FieldValueWrapper.wrap(fieldConfig.getFieldValue(targetEntity));
                    }
                }));
    }

    private StringBuilder buildFieldClause(FluentIterable<Object> transform) {
        StringBuilder fieldClauseBuilder = new StringBuilder();
        fieldClauseBuilder.append("(");
        fieldsJoiner.skipNulls().appendTo(fieldClauseBuilder, transform);
        fieldClauseBuilder.append(")");
        return fieldClauseBuilder;
    }

}
