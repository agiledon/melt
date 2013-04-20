package com.melt.config.constructor;

public class RefConstructorParameter extends ConstructorParameter {
    private String ref;

    public RefConstructorParameter(int index, String ref) {
        super(index);
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
