package com.chrissy.model.exception;

/**
 * @author chrissy
 * @Date 9/2/2024 22:39
 */
public class NoValueInGuavaException extends RuntimeException {
    public NoValueInGuavaException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
