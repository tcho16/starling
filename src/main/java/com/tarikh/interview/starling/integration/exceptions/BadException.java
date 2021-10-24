package com.tarikh.interview.starling.integration.exceptions;

public class BadException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadException(){}

    public BadException(String msg) {
        super(msg);
    }
}