package com.melt.orm.statement;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

public class DeleteStatement extends NonQueryStatement {
    public DeleteStatement(Session session) {
        super(session);
    }

    @Override
    public SqlStatement assemble(Class targetBean, Criteria criteria) {
        assembleDeleteClause(targetBean);
        assembleConditionClause(criteria);
        return this;
    }

    private  void assembleDeleteClause(Class targetBean) {
        sqlBuilder.append("delete from ");
        sqlBuilder.append(getModelConfig(targetBean).getTableName());
    }

}
