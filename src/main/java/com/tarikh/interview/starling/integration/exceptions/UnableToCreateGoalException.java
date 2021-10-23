package com.tarikh.interview.starling.integration.exceptions;

public class UnableToCreateGoalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnableToCreateGoalException(){}

    public UnableToCreateGoalException(String msg) {
        super(msg);
    }

}
