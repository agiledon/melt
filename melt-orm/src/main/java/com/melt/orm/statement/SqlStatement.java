package com.melt.orm.statement;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;

public abstract class SqlStatement {
    protected final StringBuilder sqlBuilder = new StringBuilder();
    protected Session session;

    public SqlStatement(Session session) {
        this.session = session;
    }

    public String getSql() {
        return sqlBuilder.toString();
    }

    protected void assembleConditionClause(Criteria criteria) {
        if (!criteria.isNull()) {
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(criteria.evaluate());
        }
    }

    public Session getSession() {
        return session;
    }
}
