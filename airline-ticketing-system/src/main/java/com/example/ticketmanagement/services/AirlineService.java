package com.example.ticketmanagement.services;

import com.example.ticketmanagement.models.Airline;
import com.example.ticketmanagement.models.Airplane;
import com.example.ticketmanagement.repositories.AirlineRepository;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    @Autowired
    public AirlineService(final AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public List<Airline> getAirlines() {
        Iterable<Airline> airlines = airlineRepository.findAll();
        if(airlines != null) {
            List<Airline> airlineList = new LinkedList<>();
            airlines.forEach(airline -> airlineList.add(airline));
            return airlineList;
        }
        return null;
    }

    public Airline getAirlineByName(String airlineName) {
        if(Util.validateAirlineName(airlineName)) {
            return airlineRepository.findByAirlineNameIgnoreCase(airlineName);
        }
        return null;
    }

    public Set<Airplane> getAirplanesByAirlineName(String airlineName) {
        if(Util.validateAirlineName(airlineName)) {
            Airline airline = airlineRepository.findByAirlineNameIgnoreCase(airlineName);
            if(airline != null)
                return airline.getAirplanes();
        }
        return null;
    }
}
