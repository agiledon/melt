package com.melt.orm.util;

import com.google.common.base.CaseFormat;

public class NameMapping {
    public static String getMappedName(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);

    }
}
