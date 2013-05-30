package com.melt.orm.mapping;

import com.google.common.base.Optional;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.Session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
                if (field.getFieldType().getName().equals(Integer.class.getName()) || field.getFieldType().getName().equals(Integer.TYPE.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getInt(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Double.class.getName()) || field.getFieldType().getName().equals(Double.TYPE.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getDouble(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Float.class.getName()) || field.getFieldType().getName().equals(Float.TYPE.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getFloat(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(String.class.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getString(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(BigDecimal.class.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getBigDecimal(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Date.class.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getDate(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Timestamp.class.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getTimestamp(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Long.class.getName()) || field.getFieldType().getName().equals(Long.TYPE.getName())) {
                    field.getWriter().invoke(modelObject, resultSet.getLong(field.getColumnName()));
                }

                if (field.getFieldType().getName().equals(Boolean.class.getName()) || field.getFieldType().getName().equals(Boolean.TYPE.getName())) {
                    int value = resultSet.getInt(field.getColumnName());
                    if (value == 0) {
                        field.getWriter().invoke(modelObject, false);
                    } else {
                        field.getWriter().invoke(modelObject, true);
                    }
                }

                if (field.isEnum()) {
                    String value = resultSet.getString(field.getColumnName());
                    if (value != null) {
                        field.getWriter().invoke(modelObject, Enum.valueOf(field.getFieldType(), value));
                    }
                }
            }
            if (field.isManyToOneField() || field.isOneToOneField()) {
                ModelConfig referenceModelConfig = modelConfigs.get(field.getFieldType().getName());
                Object fieldEntity = getModelObject(field.getFieldType(), referenceModelConfig);
                Method referenceEntityPrimaryKeyWriter = getReferenceEntityPrimaryKeyWriter(referenceModelConfig);
                referenceEntityPrimaryKeyWriter.invoke(fieldEntity, resultSet.getInt(field.getReferenceColumnName()));
                field.getWriter().invoke(modelObject, fieldEntity);
            }
        }
        return modelObject;
    }

    private Method getReferenceEntityPrimaryKeyWriter(ModelConfig referenceModelConfig) {
        Optional<FieldConfig> primaryKey = referenceModelConfig.getPrimaryKey();
        if (primaryKey.isPresent()) {
            FieldConfig primaryKeyFieldConfig = primaryKey.get();
            return primaryKeyFieldConfig.getWriter();
        }
        return referenceModelConfig.getFieldConfigByFieldName("id").getWriter();
    }

    private <T> T getModelObject(Class modelClass, ModelConfig modelConfig) {
        T obj = null;
        if (modelConfig.isNeedBeProxy()) {
            obj = ProxyFactory.getProxy(modelClass, session);
        } else {
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
