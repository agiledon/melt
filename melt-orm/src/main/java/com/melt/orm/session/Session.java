package com.melt.orm.session;

import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.statement.SelectStatement;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class Session {
    private Connection connection;
    private Map<String, ModelConfig> modelConfigs;

    public Session(Connection connection, Map<String, ModelConfig> modelConfigs) {
        this.connection = connection;
        this.modelConfigs = modelConfigs;
    }

    public Connection getConnection() {
        return connection;
    }

    public <T> List<T> find(Class targetEntity, Criteria criteria) {
        return executeQueryCommand(targetEntity, criteria);
    }

    public <T> T findById(Class targetEntity, int id) {
        Criteria criteria = new EqCriteria("ID", id);
        return executeQueryCommand(targetEntity, criteria);
    }

    private <T> T executeQueryCommand(Class targetEntity, Criteria criteria) {
        SelectStatement statement = new SelectStatement(this);
        statement.assemble(targetEntity, criteria);
        QueryCommand sqlCommand = statement.createQueryCommand();
        return sqlCommand.execute();
    }

    public void setModelConfigs(Map<String, ModelConfig> modelConfigs) {
        this.modelConfigs = modelConfigs;
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }
}
