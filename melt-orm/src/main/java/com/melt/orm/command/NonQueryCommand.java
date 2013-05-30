package com.melt.orm.command;

import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.InsertStatement;
import com.melt.orm.statement.NonQueryStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NonQueryCommand {
    protected Connection connection;
    protected NonQueryStatement sqlStatement;

    public NonQueryCommand(Connection connection, NonQueryStatement sqlStatement) {
        this.connection = connection;
        this.sqlStatement = sqlStatement;
    }

    public int execute() {
        try {
            Statement statement = connection.createStatement();
            return executeCommand(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected int executeCommand(Statement statement) throws SQLException {
        return statement.executeUpdate(sqlStatement.getSql());
    }

}
