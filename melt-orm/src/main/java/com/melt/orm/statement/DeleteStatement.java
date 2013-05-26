package com.melt.orm.statement;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

public class DeleteStatement extends NonQueryStatement {
    public DeleteStatement(Session session) {
        super(session);
    }

    @Override
    public <T> SqlStatement assemble(T targetBean, Criteria criteria) {
        assembleDeleteClause(targetBean);
        assembleConditionClause(criteria);
        return this;
    }

    private <T> void assembleDeleteClause(T targetBean) {
        sqlBuilder.append("delete from ");
        sqlBuilder.append(getModelConfig(targetBean).getTableName());
    }

}
