package com.example.ticketmanagement.passengers;

import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    //Save passenger into the database
    public boolean savePassenger(Passengers passengers) {
        boolean flag = false;
        if(!flag) {
            passengerRepository.save(passengers);
            return true;
        }
        return flag;
    }

    // getPassengerDetails
    public List<Passengers> getPassengerList() {
        List<Passengers> passengers = (List<Passengers>) passengerRepository.findAll();
        return passengers;
    }


    public Optional<Passengers> getPassenger(Integer passengerId) {
        Optional<Passengers> passenger = passengerRepository.findById(passengerId);
        return passenger;
    }
}
