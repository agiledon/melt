package com.melt.orm.mapping;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.Session;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class Mapper {
    private final Map<String, ModelConfig> modelConfigs;
    private Session session;

    public Mapper(Session session) {
        this.session = session;
        this.modelConfigs = session.getModelConfigs();
    }

    public <T> List<T> mapResult(Class modelClass, ResultSet resultSet) {
        ModelConfig modelConfig = modelConfigs.get(modelClass.getName());
        ArrayList<T> result = newArrayList();
        try {
            while (resultSet.next()) {
                T modelObject = mapModelObject(modelClass, resultSet, modelConfig);
                result.add(modelObject);
            }
        } catch (SQLException e) {
            throw new MeltOrmException("Has exception when mapping result");
        } catch (InvocationTargetException e) {
            throw new MeltOrmException("Has exception when mapping result");
        } catch (IllegalAccessException e) {
            throw new MeltOrmException("Has exception when mapping result");
        }
        return result;
    }

    private <T> T mapModelObject(Class modelClass, ResultSet resultSet, ModelConfig modelConfig) throws IllegalAccessException, InvocationTargetException, SQLException {
        T modelObject = getModelObject(modelClass, modelConfig);
        List<FieldConfig> fields = modelConfig.getFields();
        for (FieldConfig field : fields) {
            if (!field.isNeedBeProxy()) {
                field.getWriter().invoke(modelObject, resultSet.getObject(field.getColumnName()));
            }
        }
        return modelObject;
    }

    private <T> T getModelObject(Class modelClass, ModelConfig modelConfig)  {
        T obj = null;
        if (modelConfig.isNeedBeProxy()) {
            obj = ProxyFactory.getProxy(modelClass, session);
        }else {
            try {
                obj = (T) modelClass.newInstance();
            } catch (InstantiationException e) {
                throw new MeltOrmException(String.format("Exception when create %s model object", modelClass.getName()));
            } catch (IllegalAccessException e) {
                throw new MeltOrmException(String.format("Exception when create %s model object", modelClass.getName()));
            }
        }
        return obj;
    }
}
