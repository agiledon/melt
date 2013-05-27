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

    public abstract SqlStatement assemble(Class targetBean, Criteria criteria);

    protected ModelConfig getModelConfig(Class targetBean) {
        Map<String, ModelConfig> modelConfigs = session.getModelConfigs();
        ModelConfig modelConfig = modelConfigs.get(targetBean.getName());
        if (modelConfig == null) {
            throw new MeltOrmException("can not find model mapping.");
        }
        return modelConfig;
    }

    protected void assembleConditionClause(Criteria criteria) {
        if (!criteria.isNull()) {
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(criteria.toExpression());
        }
    }

    public Session getSession() {
        return session;
    }
}
