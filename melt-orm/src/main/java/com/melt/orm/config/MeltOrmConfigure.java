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


public class MeltOrmConfigure {
    private String modelsPackageName;
    private DatabaseDialect dialect;
    private DataSource datasource;
    private boolean isRegisteredDataSource = false;
    private boolean isRegisteredConnection = false;
    private String driver;
    private DatabaseDetail databaseDetail;

    private static MeltOrmConfigure configure;

    private MeltOrmConfigure() {
    }


    public static MeltOrmConfigure registerModels(String modelsPackageName) {
        configure = new MeltOrmConfigure();
        checkArgument(modelsPackageName != null);
        configure.modelsPackageName = modelsPackageName;
        return configure;
    }

    public MeltOrmConfigure withDialect(DatabaseDialect dialect) {
        checkArgument(dialect != null);
        this.dialect = dialect;
        return this;
    }

    public MeltOrmConfigure withDataSource(DataSource datasource) {
        if (this.isRegisteredConnection) {
            throw new MeltOrmException("Has registered a database session, please don't registered again!");
        }
        checkArgument(datasource != null);

        this.datasource = datasource;
        this.isRegisteredDataSource = true;
        return this;
    }

    public MeltOrmConfigure withDatabaseConfig(String url, String driver, String username, String password) {
        if (this.isRegisteredDataSource) {
            throw new MeltOrmException("Has registered a DataSource, please don't registered again!");
        }
        checkArgument(url != null);
        checkArgument(driver != null);
        checkArgument(username != null);
        checkArgument(password != null);

        this.driver = driver;
        this.databaseDetail = new DatabaseDetail(url, username, password);
        this.isRegisteredConnection = true;
        return this;
    }

    public SessionFactory build() {
        ModelMappingHandler modelMappingHandler = new ModelMappingHandler();
        Map<String, ModelConfig> modelConfigs = modelMappingHandler.mappingModelConfigs(modelsPackageName);
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
        try {
            Class.forName(driver);
            return new ConnectionSessionFactory(dialect, modelConfigs, databaseDetail);
        } catch (ClassNotFoundException e) {
            throw new MeltOrmException("Can't load JDBC Driver");
        }
    }
}
