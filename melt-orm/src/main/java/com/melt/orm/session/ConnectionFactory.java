package com.melt.orm.session;

public class ConnectionFactory {
    public Session openSession() {
        return new Session(null);
    }
}
