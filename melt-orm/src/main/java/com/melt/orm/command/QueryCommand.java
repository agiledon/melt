package com.melt.orm.command;

import com.melt.orm.statement.SelectStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryCommand {
    private Connection connection;
    private SelectStatement selectStatement;

    public QueryCommand(Connection connection, SelectStatement selectStatement) {
        this.connection = connection;
        this.selectStatement = selectStatement;
    }

    public <T> T execute() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement.getSql());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
