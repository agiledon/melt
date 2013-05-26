package com.melt.orm.statement;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.Session;

import java.util.Map;

public abstract class SqlStatement {
    protected final StringBuilder sqlBuilder = new StringBuilder();
    protected Session session;

    public SqlStatement(Session session) {
        this.session = session;
    }

    public String getSql() {
        return sqlBuilder.toString();
    }

    public abstract <T> SqlStatement assemble(T targetBean, Criteria criteria);

    protected <T> ModelConfig getModelConfig(T targetBean) {
        Map<String, ModelConfig> modelConfigs = session.getModelConfigs();
        ModelConfig modelConfig = modelConfigs.get(targetBean.getClass().getName());
        if (modelConfig == null) {
            throw new MeltOrmException("can not find model mapping.");
        }
        return modelConfig;
    }

    protected void assembleConditionClause(Criteria criteria) {
        if (!criteria.isNull()) {
            sqlBuilder.append(" where ");
            sqlBuilder.append(criteria.toExpression());
        }
    }
}
