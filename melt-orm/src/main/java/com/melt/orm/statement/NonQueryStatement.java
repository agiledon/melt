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

}
