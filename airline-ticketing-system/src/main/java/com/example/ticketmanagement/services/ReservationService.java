package com.example.ticketmanagement.services;/*
 *  Created by
 *   Name : kunal
 *   Date : 24-12-2023
 *   Project Name : airline-ticketing-system
 * */

import com.example.ticketmanagement.errors.InvalidRequestException;
import com.example.ticketmanagement.models.Flight;
import com.example.ticketmanagement.models.Reservation;
import com.example.ticketmanagement.models.Status;
import com.example.ticketmanagement.repositories.ReservationRepository;
import com.example.ticketmanagement.utils.Util;
import com.example.ticketmanagement.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final FlightService flightService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CustomerService customerService, FlightService flightService) {
        this.reservationRepository = reservationRepository;
        this.customerService = customerService;
        this.flightService = flightService;
    }

    public Set<Reservation> getAllRSVPsByCustomerId(Integer customerId) throws IllegalArgumentException {
        if(Util.validateNumber(customerId)) {
            Iterable<Reservation> iterable = reservationRepository.findAllRSVPByCustomerId(customerId);
            return iterableToReservationSet(iterable);
        }
        return null;
    }

    public boolean addRSVPByCustomerId(Map<String, Object> json) throws InvalidRequestException, NoSuchElementException {
        int customerId = -1;
        int flightId = -1;
        if(Util.verifyRSVPByCustomerId(json)) {
            customerId = (Integer) json.get(Util.CUSTOMER_ID_JKEY);
            Customer customer = customerService.getCustomerById(customerId);
            if(customer == null) throw new NoSuchElementException("Customer does not exists with id = " +customerId);

            flightId = ((Integer) json.get(Util.FLIGHT_ID_JKEY));
            Flight flight = flightService.getFlightById(flightId);
            if(flight == null) throw new NoSuchElementException("Flight does not exists with id = " +flightId);

            if(flight.getStatus().equals(Status.CANCELLED.toString()))
                throw new InvalidRequestException(HttpStatus.CHECKPOINT.value(), "Cannot rsvp to a cancelled flight");
            if(flight.getCapacity() > flight.getCustomers().size()){
                reservationRepository.insertRSVPByCustomerId(Util.toDBDateTime(LocalDateTime.now()), Status.ACTIVE.toString(), customerId, flightId);
                flight.getCustomers().add(customer);
                Flight updated = flightService.updateFlight(flight);
                return updated.getFlightId().equals(flight.getFlightId());
            }
            else throw new InvalidRequestException(HttpStatus.EXPECTATION_FAILED.value(), "Flight is full. Cannot dp rsvp.");
        }
        return false;
    }

    public boolean cancelRSVPByCustomerId(Integer rsvpId) throws IllegalArgumentException {
        if(Util.validateNumber(rsvpId)) {
            Optional<Reservation> optionalReservation = reservationRepository.findById(rsvpId);
            if(!optionalReservation.isPresent())
                throw new IllegalArgumentException("No reservation exists with id = " +rsvpId);
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(Status.CANCELLED);
            reservation.getFlight().getCustomers().remove(reservation.getCustomer());
            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }

    public Set<Reservation> getAllCancelledRSVPs() {
        Iterable<Reservation> iterable = reservationRepository.findReservationByStatus(Status.CANCELLED);
        return iterableToReservationSet(iterable);
    }

    public Set<Reservation> getAllActiveRSVPsByAirline(String airline) throws IllegalArgumentException {
        if(Util.validateAirlineName(airline)) {
            Iterable<Reservation> iterable = reservationRepository.findActiveReservationsByAirline(airline, Status.ACTIVE.toString());
            return iterableToReservationSet(iterable);
        }
        return null;
    }

    public Set<Reservation> getAllCancelledRSVPsByAirline(String airline) throws IllegalArgumentException {
        if(Util.validateAirlineName(airline)) {
            Iterable<Reservation> iterable = reservationRepository.findActiveReservationsByAirline(airline, Status.CANCELLED.toString());
            return iterableToReservationSet(iterable);
        }
        return null;
    }

    private Set<Reservation> iterableToReservationSet(Iterable<Reservation> iterable) {
        if(iterable != null) {
            Set<Reservation> reservations = new LinkedHashSet<>();
            iterable.forEach(reservation ->  reservations.add(reservation));
            return reservations;
        }
        return null;
    }
}
