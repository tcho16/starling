package com.tarikh.interview.starling.integration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UnableToAddMoneyToGoalException.class)
    public ResponseEntity<ErrorMessage> unableToAddMoneyToGoal(UnableToAddMoneyToGoalException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Was unable to deposit money to goal for the user");

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoPrimaryAccountsWereFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(NoPrimaryAccountsWereFoundException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "No primary accounts were found for the provided accountHolderId");

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnableToRetrieveTransactionException.class)
    public ResponseEntity<ErrorMessage> unableToRetrieveTransactions(NoPrimaryAccountsWereFoundException ex) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Was unable to retrieve the transaction for the user");

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
