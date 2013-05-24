package com.melt.orm.statement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.melt.orm.command.SelectCommand;
import com.melt.orm.command.SqlCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.Session;
import com.melt.orm.criteria.Criteria;

import java.util.Map;

import static com.google.common.collect.FluentIterable.from;

public class SelectStatement extends SqlStatement {
    private Session session;
    private final static Joiner fieldsJoiner = Joiner.on(", ");
    private final StringBuilder selectSql = new StringBuilder();

    public SelectStatement(Session session) {
        this.session = session;
    }

    public <T> SqlStatement assemble(T targetBean, Criteria criteria) {
        assembleSelectClause(getModelConfig(targetBean));
        if (!criteria.isNull()) {
            assembleConditionClause(criteria);
        }

        setSql(selectSql.toString());
        return this;
    }

    private <T> ModelConfig getModelConfig(T targetBean) {
        Map<String, ModelConfig> modelConfigs = session.getModelConfigs();
        ModelConfig modelConfig = modelConfigs.get(targetBean.getClass().getName());
        if (modelConfig == null) {
            throw new MeltOrmException("can not find model mapping.");
        }
        return modelConfig;
    }

    private void assembleSelectClause(ModelConfig modelConfig) {
        selectSql.append("select ");

        assembleFieldsClause(modelConfig);

        selectSql.append(" from ");
        selectSql.append(modelConfig.getTableName());
    }

    private void assembleConditionClause(Criteria criteria) {
        selectSql.append(" where ");
        selectSql.append(criteria.toExpression());
    }

    private void assembleFieldsClause(ModelConfig modelConfig) {
        fieldsJoiner.skipNulls().appendTo(selectSql, from(modelConfig.getFields()).transform(new Function<FieldConfig, String>() {
            @Override
            public String apply(FieldConfig input) {
                if (!input.isNeedBeProxy()) {
                    return input.getFieldName();
                }
                return null;
            }
        }));
    }

    @Override
    public SqlCommand createCommand() {
        return new SelectCommand(session.getConnection(), this);
    }
}
