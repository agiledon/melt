package com.melt.orm.session;

import java.sql.Connection;
import java.util.Map;

import com.melt.orm.command.SqlCommand;
import com.melt.orm.command.SqlCommandFactory;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.statement.SelectStatement;

import static com.google.common.collect.Maps.newHashMap;

public class Session {
    private Connection connection;
    private SqlCommandFactory commandFactory;
    private Map<String, ModelConfig> modelConfigs;

    public Session(Connection connection) {
        this.connection = connection;
        modelConfigs = newHashMap();
    }

    public Connection getConnection() {
        return connection;
    }

    public <T> T find(Class targetEntity, Criteria criteria) {
        SelectStatement statement = new SelectStatement(this);
        SqlCommand sqlCommand = statement.createCommand();
        return sqlCommand.execute();
    }

    public void setCommandFactory(SqlCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void setModelConfigs(Map<String, ModelConfig> modelConfigs) {
        this.modelConfigs = modelConfigs;
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }
}
