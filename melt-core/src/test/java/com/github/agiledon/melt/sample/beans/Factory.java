package com.github.agiledon.melt.sample.beans;

public class Factory {
    public FactoryService init(){
        return new FactoryService();
    }

    public static FactoryService classInit(){
        return new FactoryService();
    }
}
