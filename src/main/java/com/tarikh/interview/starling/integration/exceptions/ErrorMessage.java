package com.tarikh.interview.starling.integration.exceptions;

import java.util.Date;

public class ErrorMessage {

    private final int value;
    private final Date date;
    private final String message;

    public ErrorMessage(int value, Date date, String message) {
        this.value = value;
        this.date = date;
        this.message = message;
    }
}
