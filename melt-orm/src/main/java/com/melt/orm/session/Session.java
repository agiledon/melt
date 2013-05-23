package com.melt.orm.session;

import java.sql.Connection;

public class Session {
    private Connection connection;

    public Session(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
