package com.melt.orm.statement;

import com.melt.orm.command.SqlCommandFactory;

public abstract class SqlStatement implements SqlCommandFactory {
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
