package com.example.ticketmanagement.errors;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InvalidRequestException extends RuntimeException {

    private int status;
    private String message;

    public InvalidRequestException(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

}
