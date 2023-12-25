package com.example.ticketmanagement.services;/*
 *  Created by
 *   Name : kunal
 *   Date : 24-12-2023
 *   Project Name : airline-ticketing-system
 * */

import com.example.ticketmanagement.models.Status;
import com.example.ticketmanagement.repositories.FlightRepository;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticketmanagement.models.Flight;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final SourceService sourceService;
    private final DestinationService destinationService;

    @Autowired
    public FlightService(FlightRepository flightRepository, SourceService sourceService, DestinationService destinationService) {
        this.flightRepository = flightRepository;
        this.sourceService = sourceService;
        this.destinationService = destinationService;
    }

    public Flight addFlight(Flight flight) throws IllegalArgumentException {
        if(Util.validateFlight(flight) && flight.getFlightId() != null && flight.getFlightId() > 0) {
            Optional<Flight> optional = flightRepository.findById(flight.getFlightId());
            if(optional.isPresent()) throw new IllegalArgumentException("A flight already exists with id = " +flight.getFlightId());
        }
        flight.setStatus(Status.ON_TIME);
        flight.setSource(sourceService.addSource(flight.getSource()));
        flight.setDestination(destinationService.addDestination(flight.getDestination()));
        Flight addedFlight = flightRepository.save(flight);
        addedFlight.setAvailableSeat(addedFlight.getCapacity());
        return addedFlight;
    }

    public boolean cancelFlight(Integer flightId) throws IllegalArgumentException {
        if(Util.validateNumber(flightId)) {
            Optional<Flight> optionalFlight = flightRepository.findById(flightId);
            if(!optionalFlight.isPresent()) throw new IllegalArgumentException("Flight does not exists with id = " +flightId);
            else {
                Flight flight = optionalFlight.get();
                if(flight.getCustomers().size() <= 0) {
                    flightRepository.changeFlightStatus(Status.CANCELLED.toString(), flightId);
                    return true;
                }
                else {
                    flightRepository.deleteCustomerRSVPByFlightId(flightId, Status.CANCELLED.toString());
                    return true;
                }
            }
        }
        return false;
    }

    public Flight getFlightById(Integer flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if(optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            flight.setAvailableSeat(flight.getCapacity()-flight.getCustomers().size());
            return flight;
        }
        return null;
    }

    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Set<Flight> getFlights() {
        return iterableToSet(flightRepository.findAll());
    }

    public Set<Flight> getFlightsForToday() {
        LocalDateTime dateTime = Util.toDBDateTime(LocalDateTime.now());
        return iterableToSet(flightRepository.findByCurrentDateTime(dateTime, dateTime.plusHours(23)));
    }

    public Set<Flight> getFlightByDate(LocalDateTime localDateTime) {
        return iterableToSet(flightRepository.findByDate(Util.toDBDateTime(localDateTime)));
    }

    public Set<Flight> getFlightByStats(String status) {
        try {
            Status eStatus = Util.validateFlightStatus(status);
            return iterableToSet(flightRepository.findFlightsByStatus(eStatus.toString()));
        } catch(IllegalArgumentException ex) {
            throw ex;
        }
    }

    public Set<Flight> getFlightByFare(Float fare) {
        if(Util.validateNumber(fare)) {
            return flightRepository.findByFare(fare);
        }
        return null;
    }

    private Set<Flight> iterableToSet(Iterable<Flight> iterable) {
        if(iterable == null) return null;
        else {
            Set<Flight> flights = new LinkedHashSet<>();
            iterable.forEach(flight -> flights.add(flight));
            return flights;
        }
    }

}
