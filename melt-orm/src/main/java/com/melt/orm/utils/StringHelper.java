package com.melt.orm.utils;

import com.google.common.base.CaseFormat;

public class StringHelper {
    public static String splitWordsByUpperCaseChar(String words) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, words);
    }
}