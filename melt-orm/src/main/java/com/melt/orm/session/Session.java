package com.melt.orm.session;

import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.By;
import com.melt.orm.criteria.Criteria;
import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.DeleteStatement;
import com.melt.orm.statement.SelectStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.criteria.By.nil;

public class Session {
    private final InsertExecutor insertExecutor = new InsertExecutor(this);
    private final UpdateExecutor updateExecutor = new UpdateExecutor(this);
    private Connection connection;
    private Map<String, ModelConfig> modelConfigs;

    public Session(Connection connection, Map<String, ModelConfig> modelConfigs) {
        this.connection = connection;
        this.modelConfigs = modelConfigs;
    }

    public Connection getConnection() {
        return connection;
    }

    public <T> List<T> findAll(Class targetEntityClass) {
        return find(targetEntityClass, nil());
    }

    public <T> List<T> find(Class targetEntityClass, Criteria criteria) {
        return executeQueryCommand(targetEntityClass, criteria);
    }

    public <T> T findById(Class targetEntityClass, int id) {
        Criteria criteria = new EqCriteria("id", id);
        List<T> objects = executeQueryCommand(targetEntityClass, criteria);
        if (objects.size() > 0) {
            return objects.get(0);
        }
        throw new MeltOrmException(String.format("Entity not found by id: %d", id));
    }

    public <T> int update(T targetEntity) {
        return update(targetEntity, nil());
    }

    public <T> int update(T targetEntity, Criteria criteria) {
        return updateExecutor.execute(targetEntity, criteria);
    }

    public <T> int insert(T targetEntity) {
        return insertExecutor.execute(targetEntity);
    }

    public int delete(Class targetEntityClass, Criteria criteria) {
        return executeDeleteCommand(targetEntityClass, criteria);
    }

    public int deleteById(Class targetEntityClass, int id) {
        return delete(targetEntityClass, By.id(id));
    }

    public int deleteAll(Class targetEntityClass) {
        return delete(targetEntityClass, nil());
    }

    private int executeDeleteCommand(Class targetEntityClass, Criteria criteria) {
        DeleteStatement statement = new DeleteStatement(this);
        statement.assemble(targetEntityClass, criteria);
        return statement.createNonQueryCommand().execute();
    }

    private <T> List<T> executeQueryCommand(Class targetEntityClass, Criteria criteria) {
        SelectStatement statement = new SelectStatement(this);
        statement.assemble(targetEntityClass, criteria);
        QueryCommand sqlCommand = statement.createQueryCommand();
        return sqlCommand.execute();
    }

    public Map<String, ModelConfig> getModelConfigs() {
        return modelConfigs;
    }

    public ModelConfig getModelConfig(Class targetBean) {
        Map<String, ModelConfig> modelConfigs = getModelConfigs();
        ModelConfig modelConfig = modelConfigs.get(getBeanClassName(targetBean));
        if (modelConfig == null) {
            throw new MeltOrmException("can not find model mapping.");
        }
        return modelConfig;
    }

    private String getBeanClassName(Class targetBean) {
        String className = targetBean.getName();
        if (className.contains("$$")){
            int end = className.indexOf("$$");
            return className.substring(0, end);
        }
        return className;
    }
}
