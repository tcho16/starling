package com.tarikh.interview.starling.integration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnableToRetreiveGoalsException.class)
    public ResponseEntity<ErrorDTO> unableToRetrieveGoal(UnableToRetreiveGoalsException ex) {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<ErrorDTO>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToAddMoneyToGoalException.class)
    public ResponseEntity<ErrorDTO> unableToAddMoneyToGoal(UnableToAddMoneyToGoalException ex) {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<ErrorDTO>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoPrimaryAccountsWereFoundException.class)
    public ResponseEntity<ErrorDTO> resourceNotFoundException(NoPrimaryAccountsWereFoundException ex) {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<ErrorDTO>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnableToRetrieveTransactionException.class)
    public ResponseEntity<ErrorDTO> unableToRetrieveTransactions(UnableToRetrieveTransactionException ex) {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
