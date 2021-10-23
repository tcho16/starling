package com.tarikh.interview.starling.integration.exceptions;

public class UnableToAddMoneyToGoalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnableToAddMoneyToGoalException(){}

    public UnableToAddMoneyToGoalException(String msg) {
        super(msg);
    }
}
