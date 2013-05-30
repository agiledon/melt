package com.melt.orm.statement;

import com.melt.orm.command.NonQueryCommand;
import com.melt.orm.session.Session;

import static com.google.common.collect.ObjectArrays.newArray;

public abstract class NonQueryStatement extends SqlStatement {
    public NonQueryStatement(Session session) {
        super(session);
    }

    public NonQueryCommand createNonQueryCommand() {
        return new NonQueryCommand(session.getConnection(), this);
    }

    protected void replaceAll(StringBuilder builder, String from, String to)
    {
        int index = builder.indexOf(from);
        while (index != -1)
        {
            builder.replace(index, index + from.length(), to);
            index += to.length();
            index = builder.indexOf(from, index);
        }
    }

    public void setForeignKey(String referenceColumnName, int foreignKey) {
        String variableName = String.format("${%s}", referenceColumnName);
        replaceAll(sqlBuilder, variableName, String.valueOf(foreignKey));
    }
}
