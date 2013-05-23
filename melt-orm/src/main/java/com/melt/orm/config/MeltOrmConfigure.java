package com.melt.orm.config;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.config.parser.ModelMappingHandler;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.ConnectionSessionFactory;
import com.melt.orm.session.DataSourceSessionFactory;
import com.melt.orm.session.SessionFactory;

import javax.sql.DataSource;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class MeltOrmConfigure {
    private String modelsPackageName;
    private DatabaseDialect dialect;
    private DataSource datasource;
    private String url;
    private String username;
    private String password;
    private boolean isRegisteredDataSource = false;
    private boolean isRegisteredConnection = false;
    private String driver;
    private final ModelMappingHandler modelMappingHandler = new ModelMappingHandler();
    private DatabaseDetail databaseDetail;

    public MeltOrmConfigure registerModels(String modelsPackageName) {
        this.modelsPackageName = modelsPackageName;
        return this;
    }

    public MeltOrmConfigure withDialect(DatabaseDialect dialect) {
        this.dialect = dialect;
        return this;
    }

    public MeltOrmConfigure withDataSource(DataSource datasource) {
        if (this.isRegisteredDataSource) {
            throw new MeltOrmException("Has registered a database session, please don't registered again!");
        }
        this.datasource = datasource;
        this.isRegisteredDataSource = true;
        return this;
    }

    public MeltOrmConfigure withDatabaseConfig(String url, String driver, String username, String password) {
        if (this.isRegisteredDataSource) {
            throw new MeltOrmException("Has registered a DataSource, please don't registered again!");
        }
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.databaseDetail = new DatabaseDetail(url, username, password);
        this.isRegisteredConnection = true;
        return this;
    }

    public SessionFactory build() {
        checkArgument(dialect != null);
        checkArgument(modelsPackageName != null);
        Map<String,ModelConfig> modelConfigs = modelMappingHandler.mappingModelConfigs(modelsPackageName);
        if (isRegisteredConnection) {
            return createConnectionSessionFactory(modelConfigs);
        } else if (isRegisteredDataSource) {
            return createDataSourceSessionFactory(modelConfigs);
        }
        throw new MeltOrmException("Please register database information.");
    }

    private DataSourceSessionFactory createDataSourceSessionFactory(Map<String, ModelConfig> modelConfigs) {
        return new DataSourceSessionFactory(dialect, modelConfigs, datasource);
    }

    private ConnectionSessionFactory createConnectionSessionFactory(Map<String, ModelConfig> modelConfigs) {
        checkNotNull(url);
        checkNotNull(driver);
        checkNotNull(username);
        checkNotNull(password);
        try {
            Class.forName(driver);
            return new ConnectionSessionFactory(dialect, modelConfigs, databaseDetail);
        } catch (ClassNotFoundException e) {
            throw new MeltOrmException("Can't load JDBC Driver");
        }
    }
}
