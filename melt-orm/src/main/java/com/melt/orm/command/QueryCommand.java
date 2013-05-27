package com.melt.orm.command;

import com.melt.orm.mapping.Mapper;
import com.melt.orm.statement.SelectStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class QueryCommand {
    private Connection connection;
    private SelectStatement selectStatement;
    private Mapper mapper;

    public QueryCommand(Connection connection, SelectStatement selectStatement) {
        this.connection = connection;
        this.selectStatement = selectStatement;
        this.mapper = new Mapper(selectStatement.getSession());
    }

    public <T> T execute() {
        try {
            Statement statement = connection.createStatement();
            String sql = selectStatement.getSql();
            System.out.println(sql.toUpperCase());
            ResultSet resultSet = statement.executeQuery(sql);
//            return mapper.mapResult()
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
