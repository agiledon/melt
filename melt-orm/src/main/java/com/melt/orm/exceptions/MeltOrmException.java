package com.melt.orm.exceptions;

public class MeltOrmException extends RuntimeException {
    public MeltOrmException(Throwable throwable) {
        super(throwable);
    }

    public MeltOrmException(String message) {
        super(message);
    }
}
