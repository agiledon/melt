package com.melt.config.constructor;

public class ConstructorParameter {
    private int index;
    private String ref;
    private String value;

    public ConstructorParameter(int index, String ref) {
        this.index = index;
        this.ref = ref;
    }


    public int getIndex() {
        return index;
    }

    public String getRef() {
        return ref;
    }
}
