package com.example.ticketmanagement.passengers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PassengerNotFoundException extends RuntimeException{

    public PassengerNotFoundException(String message) {
        super(message);
    }
}
