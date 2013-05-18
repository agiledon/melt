package com.melt.orm.config;

import org.junit.Test;

public class MeltOrmConfigureTest {
    @Test
    public void should_config_models(){
        MeltOrmConfigure configure = new MeltOrmConfigure();
        configure.registerModels("sample.model");
    }
}
