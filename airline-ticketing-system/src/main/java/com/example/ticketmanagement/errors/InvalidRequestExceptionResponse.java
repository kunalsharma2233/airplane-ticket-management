package com.example.ticketmanagement.errors;

public class InvalidRequestExceptionResponse extends RuntimeException {

    private int status;
    private String message;

    public InvalidRequestExceptionResponse(InvalidRequestException ex) {
        this.status = ex.getStatus();
        this.message = ex.getMessage();
    }

    public InvalidRequestExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
