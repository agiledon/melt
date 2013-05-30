package com.melt.orm.statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import com.melt.orm.criteria.Criteria;

import static com.google.common.collect.FluentIterable.from;
import static com.melt.orm.criteria.By.nil;

public class SelectStatement extends SqlStatement {
    private final static Joiner fieldsJoiner = Joiner.on(", ");
    private Class targetEntity;

    public SelectStatement(Session session) {
        super(session);
    }

    public SqlStatement assemble(Class targetEntity, Criteria criteria) {
        this.targetEntity = targetEntity;
        assembleSelectClause(session.getModelConfig(targetEntity));
        assembleConditionClause(criteria);
        return this;
    }

    public SqlStatement assemble(Class targetEntity) {
        return assemble(targetEntity, nil());
    }

    private void assembleSelectClause(ModelConfig modelConfig) {
        sqlBuilder.append("SELECT ");
        assembleFieldsClause(modelConfig);
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(modelConfig.getTableName());
    }

    private void assembleFieldsClause(ModelConfig modelConfig) {
        fieldsJoiner.skipNulls().appendTo(sqlBuilder, from(modelConfig.getFields()).transform(new Function<FieldConfig, String>() {
            @Override
            public String apply(FieldConfig input) {
                if (!input.isNeedBeProxy()) {
                    return input.getColumnName();
                }
                if (input.isManyToOneField() || input.isOneToOneField()) {
                    return input.getReferenceColumnName();
                }
                return null;
            }
        }));
    }

    public QueryCommand createQueryCommand() {
        return new QueryCommand(session.getConnection(), this);
    }

    public Class getTargetEntity() {
        return targetEntity;
    }
}
