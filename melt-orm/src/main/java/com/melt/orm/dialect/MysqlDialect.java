package com.melt.orm.dialect;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.exceptions.MeltOrmException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class MysqlDialect implements DatabaseDialect {
    private final static Map<String, String> TYPE_MAPPINGS = newHashMap();

    static {
        TYPE_MAPPINGS.put(String.class.getName(), "TEXT");
        TYPE_MAPPINGS.put(Integer.TYPE.getName(), "TINYINT UNSIGNED");
        TYPE_MAPPINGS.put(Integer.class.getName(), "TINYINT UNSIGNED");
        TYPE_MAPPINGS.put(Long.TYPE.getName(), "INTEGER UNSIGNED");
        TYPE_MAPPINGS.put(Long.class.getName(), "INTEGER UNSIGNED");
        TYPE_MAPPINGS.put(Long.TYPE.getName(), "INTEGER UNSIGNED");
        TYPE_MAPPINGS.put(Long.class.getName(), "INTEGER UNSIGNED");
        TYPE_MAPPINGS.put(Boolean.TYPE.getName(), "TINYINT UNSIGNED");
        TYPE_MAPPINGS.put(Boolean.class.getName(), "TINYINT UNSIGNED");
        TYPE_MAPPINGS.put(Float.TYPE.getName(), "FLOAT");
        TYPE_MAPPINGS.put(Float.class.getName(), "FLOAT");
        TYPE_MAPPINGS.put(Double.TYPE.getName(), "DOUBLE");
        TYPE_MAPPINGS.put(Double.class.getName(), "DOUBLE");
        TYPE_MAPPINGS.put(BigDecimal.class.getName(), "DECIMAL");
        TYPE_MAPPINGS.put(Date.class.getName(), "DATE");
        TYPE_MAPPINGS.put(Timestamp.class.getName(), "DATETIME");
        TYPE_MAPPINGS.put(byte[].class.getName(), "BINARY");
    }

    @Override
    public String mappingFieldType(Class fieldType) {
        if (!isBasicType(fieldType)) {
            throw new MeltOrmException(String.format("The type %s is not support by MySQL", fieldType.getName()));
        }
        return TYPE_MAPPINGS.get(fieldType.getName());
    }

    @Override
    public boolean isBasicType(Class fieldType) {
        return TYPE_MAPPINGS.containsKey(fieldType.getName());
    }
}
