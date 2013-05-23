package com.melt.orm.session;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public abstract class SessionFactory {
    private DatabaseDialect dialect;
    private Map<String, ModelConfig> modelConfigs;

    public SessionFactory(DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs) {
        this.dialect = dialect;
        this.modelConfigs = modelConfigs;
    }

    public Session createSession() {
        try {
            return new Session(getConnection());
        } catch (SQLException e) {
            throw new MeltOrmException("Can't get connection from dataSource");
        }
    }

    protected abstract Connection getConnection() throws SQLException;

    public DatabaseDialect getDialect() {
        return dialect;
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }
}
