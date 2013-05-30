package com.melt.orm.session;

import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.InsertStatement;
import com.melt.orm.statement.SelectStatement;
import com.melt.orm.statement.UpdateStatement;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

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

    public <T> int insert(T targetEntity) {

        ModelConfig modelConfig = getModelConfig(targetEntity.getClass());
        Map<String, Integer> manyToOne = newHashMap();

        for (FieldConfig fieldConfig : modelConfig.getFields()) {
            if (fieldConfig.isManyToOneField()) {
                Object fieldValue = fieldConfig.getFieldValue(targetEntity);
                InsertStatement statement = new InsertStatement(this);
                statement.assemble(fieldValue);
                int foreignKey = statement.createNonQueryCommand().execute();
                manyToOne.put(fieldConfig.getReferenceColumnName(), foreignKey);
            }
        }


        InsertStatement statement = new InsertStatement(this);
        statement.assemble(targetEntity);
        for (Map.Entry<String, Integer> foreignKey : manyToOne.entrySet()) {
            statement.setForeignKey(foreignKey.getKey(), foreignKey.getValue());
        }



        int primaryKey = statement.createNonQueryCommand().execute();


        return 0;
    }

    private <T> List<T> executeQueryCommand(Class targetEntity, Criteria criteria) {
        SelectStatement statement = new SelectStatement(this);
        statement.assemble(targetEntity, criteria);
        QueryCommand sqlCommand = statement.createQueryCommand();
        return sqlCommand.execute();
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }

    public ModelConfig getModelConfig(Class targetBean) {
        Map<String, ModelConfig> modelConfigs = getModelConfigs();
        ModelConfig modelConfig = modelConfigs.get(targetBean.getName());
        if (modelConfig == null) {
            throw new MeltOrmException("can not find model mapping.");
        }
        return modelConfig;
    }
}
