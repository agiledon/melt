package com.melt.orm.session;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;

public abstract class SessionFactory {
    private DatabaseDialect dialect;
    private Map<String, ModelConfig> modelConfigs;
    private static final Logger logger = LoggerFactory.getLogger(SessionFactory.class);

    public SessionFactory(DatabaseDialect dialect, Map<String, ModelConfig> modelConfigs) {
        this.dialect = dialect;
        this.modelConfigs = modelConfigs;
    }

    public Session createSession() {
        try {
            Session session = new Session(getConnection());
            session.setModelConfigs(modelConfigs);
            return session;
        } catch (SQLException e) {
            throw new MeltOrmException("Can't get connection from the database configuration");
        }
    }

    public void createTables() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            for (ModelConfig modelConfig : modelConfigs.values()) {
                execute(statement, modelConfig.generateDropTableSQL());
                execute(statement, modelConfig.generateCreateTableSQL(dialect, modelConfigs));
            }
        } catch (SQLException e) {
            throw new MeltOrmException("Can't get connection from the database configuration");
        }
    }

    public void showCreateTablesSQL() {
        String sql = Joiner.on("\n").join(
                from(modelConfigs.values())
                        .transform(new Function<ModelConfig, String>() {
                            @Override
                            public String apply(ModelConfig modelConfig) {
                                return modelConfig.generateDropTableSQL() + ";\n" + modelConfig.generateCreateTableSQL(dialect, modelConfigs) + ";\n";
                            }
                        }));
        System.out.println(sql);
    }

    private boolean execute(Statement statement, String sql) throws SQLException {
        logger.debug(sql);
        return statement.execute(sql);
    }

    public DatabaseDialect getDialect() {
        return dialect;
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }

    protected abstract Connection getConnection() throws SQLException;
}
