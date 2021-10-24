package com.tarikh.interview.starling.integration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

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

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ErrorDTO> accessTokenHasExpired(AccessTokenExpiredException ex)
    {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadException.class)
    public ResponseEntity<ErrorDTO> errorWithBadRequest(BadException ex)
    {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorDTO> errorParsingTheDate(DateTimeParseException ex)
    {
        ErrorDTO message = new ErrorDTO(
                ex.getMessage() + ". Ensure you provide the date + time in the format 2021-10-18 21:46:07",
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
