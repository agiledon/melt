package com.melt.orm.statement;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

public class UpdateStatement extends NonQueryStatement {
    public UpdateStatement(Session session) {
        super(session);
    }

    @Override
    public SqlStatement assemble(Class targetBean, Criteria criteria) {
        sqlBuilder.append("update ");
        sqlBuilder.append(getModelConfig(targetBean).getTableName());
        sqlBuilder.append(" set ");

        return this;
    }
}
