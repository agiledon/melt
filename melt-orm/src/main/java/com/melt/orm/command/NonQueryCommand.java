package com.melt.orm.command;

import com.melt.orm.statement.NonQueryStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NonQueryCommand  {
    private Connection connection;
    private NonQueryStatement sqlStatement;

    public NonQueryCommand(Connection connection, NonQueryStatement sqlStatement) {
        this.connection = connection;
        this.sqlStatement = sqlStatement;
    }

    public int execute() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sqlStatement.getSql());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
