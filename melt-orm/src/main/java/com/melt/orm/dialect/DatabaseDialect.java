package com.melt.orm.dialect;

import com.melt.orm.config.parser.ModelConfig;

public interface DatabaseDialect {
    String mappingFieldType(Class fieldType);

    boolean isBasicType(Class fieldType);

    String generateInsertSQL(ModelConfig modelConfig);
}
