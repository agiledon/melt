package com.melt.sample.bank.beans;

public class Factory {
    public FactoryService init(){
        return new FactoryService();
    }

    public static FactoryService classInit(){
        return new FactoryService();
    }
}
