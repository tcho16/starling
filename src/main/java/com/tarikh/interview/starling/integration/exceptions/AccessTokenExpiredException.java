package com.tarikh.interview.starling.integration.exceptions;

public class AccessTokenExpiredException  extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AccessTokenExpiredException(){}

    public AccessTokenExpiredException(String msg) {
        super(msg);
    }
}
