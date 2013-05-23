package com.melt.orm.session;

import com.melt.orm.config.DatabaseDetail;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.DatabaseDialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class ConnectionSessionFactory extends SessionFactory {
    private DatabaseDetail databaseDetail;

    public ConnectionSessionFactory(DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs, DatabaseDetail databaseDetail) {
        super(dialect, modelConfigs);
        this.databaseDetail = databaseDetail;
    }


    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                databaseDetail.getUrl(), databaseDetail.getUsername(), databaseDetail.getPassword());
    }
}
