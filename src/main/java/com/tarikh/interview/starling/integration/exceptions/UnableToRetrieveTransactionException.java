package com.tarikh.interview.starling.integration.exceptions;

public class UnableToRetrieveTransactionException extends RuntimeException {
    public UnableToRetrieveTransactionException(){}

    public UnableToRetrieveTransactionException(String msg, Throwable throwable)
    {
        super(msg, throwable);
    }

    public UnableToRetrieveTransactionException(String msg) {
        super(msg);
    }
}
