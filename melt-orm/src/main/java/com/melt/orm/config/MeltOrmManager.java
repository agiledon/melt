package com.melt.orm.config;

import com.melt.orm.dialect.MySQLDialect;
import com.melt.orm.session.SessionFactory;

import javax.sql.DataSource;

import static com.melt.orm.config.MeltOrmConfigure.registerModels;

public class MeltOrmManager {
    private String modelPackage;
    private String url;
    private String driver;
    private String username;
    private String password;


    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SessionFactory createSessionFactory() {
        return registerModels(modelPackage)
                .withDialect(new MySQLDialect())
                .withDatabaseConfig(url, driver, username, password)
                .build();
    }
}
