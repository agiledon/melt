package com.melt.orm.statement;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

import static com.melt.orm.criteria.By.nil;

public class DeleteStatement extends NonQueryStatement {
    public DeleteStatement(Session session) {
        super(session);
    }

    public SqlStatement assemble(Class targetEntity, Criteria criteria) {
        assembleDeleteClause(targetEntity);
        assembleConditionClause(criteria);
        return this;
    }

    public SqlStatement assemble(Class targetEntity) {
        return assemble(targetEntity, nil());
    }

    private  void assembleDeleteClause(Class targetBean) {
        sqlBuilder.append("DELETE FROM ");
        sqlBuilder.append(getModelConfig(targetBean).getTableName());
    }

}
