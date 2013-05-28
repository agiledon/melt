package com.melt.orm.session;

import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.SelectStatement;
import com.melt.orm.statement.UpdateStatement;

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
        Criteria criteria = new EqCriteria("id", id);
        List<T> objects = executeQueryCommand(targetEntity, criteria);
        if (objects.size() > 0) {
            return objects.get(0);
        }
        throw new MeltOrmException(String.format("Entity not found by id: %d", id));
    }

    public <T> int update(T targetEntity, Criteria criteria) {
        UpdateStatement statement = new UpdateStatement(this);
        statement.assemble(targetEntity, criteria);
        return statement.createNonQueryCommand().execute();
    }

    private <T> List<T> executeQueryCommand(Class targetEntity, Criteria criteria) {
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
