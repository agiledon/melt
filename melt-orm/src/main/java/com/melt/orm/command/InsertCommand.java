package com.melt.orm.command;

import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.InsertStatement;
import com.melt.orm.statement.NonQueryStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertCommand extends NonQueryCommand {
    public InsertCommand(Connection connection, NonQueryStatement sqlStatement) {
        super(connection, sqlStatement);
    }

    @Override
    protected int executeCommand(Statement statement) throws SQLException {
        return executeInsertCommand(statement);
    }


    private int executeInsertCommand(Statement statement) throws SQLException {
        statement.executeUpdate(sqlStatement.getSql(), Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new MeltOrmException("Creating failed, no generated key obtained.");
        }
    }
}
