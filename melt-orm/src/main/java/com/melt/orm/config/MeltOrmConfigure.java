package com.melt.orm.config;

public class MeltOrmConfigure {
    private String modelsPackageName;

    public MeltOrmConfigure registerModels(String modelsPackageName) {
        this.modelsPackageName = modelsPackageName;
        return this;
    }



    public SessionFactory build(){
        return new SessionFactory();
    }
}
