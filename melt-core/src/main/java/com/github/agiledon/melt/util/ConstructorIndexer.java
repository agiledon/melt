package com.github.agiledon.melt.util;

public class ConstructorIndexer {
    private static int index = 0;

    public static int index() {
        return index++;
    }

    public static void reset() {
        index = 0;
    }
}