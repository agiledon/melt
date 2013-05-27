package com.melt.orm.statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import com.melt.orm.criteria.Criteria;

import static com.google.common.collect.FluentIterable.from;

public class SelectStatement extends SqlStatement {
    private final static Joiner fieldsJoiner = Joiner.on(", ");

    public SelectStatement(Session session) {
        super(session);
    }

    @Override
    public SqlStatement assemble(Class targetBean, Criteria criteria) {
        assembleSelectClause(getModelConfig(targetBean));
        assembleConditionClause(criteria);
        return this;
    }

    private void assembleSelectClause(ModelConfig modelConfig) {
        sqlBuilder.append("select ");

        assembleFieldsClause(modelConfig);

        sqlBuilder.append(" from ");
        sqlBuilder.append(modelConfig.getTableName());
    }

    private void assembleFieldsClause(ModelConfig modelConfig) {
        fieldsJoiner.skipNulls().appendTo(sqlBuilder, from(modelConfig.getFields()).transform(new Function<FieldConfig, String>() {
            @Override
            public String apply(FieldConfig input) {
                if (!input.isNeedBeProxy()) {
                    return input.getColumnName();
                }
                return null;
            }
        }));
    }

    public QueryCommand createQueryCommand() {
        return new QueryCommand(session.getConnection(), this);
    }
}
