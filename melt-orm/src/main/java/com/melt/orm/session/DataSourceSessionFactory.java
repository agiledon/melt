package com.melt.orm.session;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DataSourceSessionFactory extends SessionFactory{
    private DataSource dataSource;

    public DataSourceSessionFactory(DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs, DataSource dataSource) {
        super(dialect, modelConfigs);
        this.dataSource = dataSource;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
