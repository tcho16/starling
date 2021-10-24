package com.tarikh.interview.starling.integration.exceptions;

import java.util.function.Supplier;

public class NoPrimaryAccountsWereFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoPrimaryAccountsWereFoundException(){}

    public NoPrimaryAccountsWereFoundException(String msg) {
        super(msg);
    }
}
