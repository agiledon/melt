package com.melt.orm.mapping;

import com.google.common.base.Optional;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.MySQLDialect;
import com.melt.orm.session.Session;
import com.melt.orm.session.SessionFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.melt.orm.criteria.By.eq;

public class ProxyCallback implements MethodInterceptor {
    private Map<String, ModelConfig> modelConfigs;
    private Session session;
    private Class modelClass;

    public ProxyCallback(Session session, Class modelClass) {
        this.modelClass = modelClass;
        this.modelConfigs = session.getModelConfigs();
        this.session = session;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();
        ModelConfig modelConfig = modelConfigs.get(modelClass.getName());
        FieldConfig fieldConfig = modelConfig.getFieldConfigByMethodName(methodName);
        if (fieldConfig != null && fieldConfig.isNeedBeProxy() && methodName.startsWith("get")) {
            Integer primaryKeyValue = getPrimaryKeyValue(obj, modelConfig);
            if (fieldConfig.isOneToManyField()) {
                String fieldClassName = fieldConfig.getGenericType().getName();
                ModelConfig fieldModelConfig = modelConfigs.get(fieldClassName);
                FieldConfig referenceFieldConfig = fieldModelConfig.getFieldConfigByFieldName(getReferenceFieldName(modelClass.getSimpleName()));
                List<Object> entities = session.find(fieldModelConfig.getModelClass(), eq(modelConfig.getReferenceColumnName(), primaryKeyValue));
                for (Object entity : entities) {
                    referenceFieldConfig.getWriter().invoke(entity, obj);
                }
                if (fieldConfig.getFieldType().getName().equals(Set.class.getName())) {
                    return newHashSet(entities);
                }

                if (fieldConfig.getFieldType().isArray()) {
                    return entities.toArray();
                }
                return entities;
            }  else {
                Object referenceEntity = proxy.invokeSuper(obj, args);
                ModelConfig referenceEntityModelConfig = modelConfigs.get(fieldConfig.getFieldType().getName());
                Integer referenceKeyValue = getPrimaryKeyValue(referenceEntity, referenceEntityModelConfig);
                return session.findById(fieldConfig.getFieldType(), referenceKeyValue);
            }
        } else {
            return proxy.invokeSuper(obj, args);
        }
    }

    private String getReferenceFieldName(String simpleName) {
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1, simpleName.length());
    }

    private Integer getPrimaryKeyValue(Object obj, ModelConfig modelConfig) throws IllegalAccessException, InvocationTargetException {
        FieldConfig primaryKeyField = getPrimaryKeyField(modelConfig);
        Method primaryKeyFieldReader = primaryKeyField.getReader();
        return (Integer) primaryKeyFieldReader.invoke(obj);
    }

    private FieldConfig getPrimaryKeyField(ModelConfig modelConfig) {
        Optional<FieldConfig> primaryKey = modelConfig.getPrimaryKey();
        if (primaryKey.isPresent()) {
            return primaryKey.get();
        }else {
            return modelConfig.getFieldConfigByFieldName("id");
        }
    }
}
