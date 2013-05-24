package com.melt.orm.command;

import com.melt.orm.statement.SqlStatement;

public interface SqlCommand {
    <T> T execute();
}
