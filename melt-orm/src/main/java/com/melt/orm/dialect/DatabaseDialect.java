package com.melt.orm.dialect;

public interface DatabaseDialect {
    String mappingFieldType(Class fieldType);

    boolean isBasicType(Class fieldType);

    String getAutoIncreaseColumn();
}
