package com.melt.orm.statement;

import com.melt.orm.command.NonQueryCommand;
import com.melt.orm.command.QueryCommand;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.session.Session;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.collect.ObjectArrays.newArray;

public abstract class NonQueryStatement extends SqlStatement {
    public NonQueryStatement(Session session) {
        super(session);
    }

    public NonQueryCommand createNonQueryCommand() {
        return new NonQueryCommand(session.getConnection(), this);
    }

    protected <T> Object getFieldValue(T targetEntity, FieldConfig field) {
        Object fieldValue;
        try {
            fieldValue = field.getReader().invoke(targetEntity, newArray(Object.class, 0));
        } catch (IllegalAccessException e) {
            fieldValue = null;
        } catch (InvocationTargetException e) {
            fieldValue = null;
        }
        return fieldValue;
    }
}
