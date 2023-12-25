package com.example.ticketmanagement.rest;/*
 *  Created by
 *   Name : kunal
 *   Date : 24-12-2023
 *   Project Name : airline-ticketing-system
 * */

import com.example.ticketmanagement.errors.InvalidRequestException;
import com.example.ticketmanagement.errors.InvalidRequestExceptionResponse;
import com.example.ticketmanagement.models.*;
import com.example.ticketmanagement.services.*;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/public")
public class RESTController {

    private final AirportService airportService;
    private final AirlineService airlineService;
    private final AirplaneService airplaneService;
    private final CustomerService customerService;
    private final FlightService flightService;
    private final ReservationService reservationService;

    @Autowired
    public RESTController(AirportService airportService,
                          AirlineService airlineService,
                          AirplaneService airplaneService,
                          CustomerService customerService,
                          FlightService flightService,
                          ReservationService reservationService) {
        this.airportService = airportService;
        this.airlineService = airlineService;
        this.airplaneService = airplaneService;
        this.customerService = customerService;
        this.flightService = flightService;
        this.reservationService = reservationService;
    }

    @GetMapping(value = "/airports", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Airport>> getAirports() {
        List<Airport> airports = airportService.getAirports();
        return airports != null ? new ResponseEntity<>(airports, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/airport/{airportName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airport> getAirportByName(@PathVariable String airportName) {
        try {
            Airport airport = null;
            if(Util.validateAirportName(airportName)) {
              airport = airportService.getAirportByName(airportName);
            }
            return airport != null ? new ResponseEntity<>(airport, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping(value = "/airlines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Airline>> getAirlines() {
        List<Airline> airlines = airlineService.getAirlines();
        return airlines != null ? new ResponseEntity<>(airlines, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/airline/{airlineName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airline> getAirlineByName(@PathVariable String airlineName) {
        try {
            Airline airline = null;
            if(Util.validateAirlineName(airlineName)) {
                airline = airlineService.getAirlineByName(airlineName);
            }
            return airline != null ? new ResponseEntity<>(airline, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }   catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping(value = "/airline/{airlineName}/airplanes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Airplane>> getAirplanesByAirlineName(@PathVariable String airlineName) {
        try {
            Set<Airplane> airplanes = null;
            if(Util.validateAirlineName(airlineName)) {
                airplanes = airlineService.getAirplanesByAirlineName(airlineName);
            }
            return airplanes != null ? new ResponseEntity<>(airplanes, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping(value = "/airplanes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Airplane>> getAirplanes() {
        List<Airplane> airplanes = airplaneService.getAirplanes();
        return airplanes != null ? new ResponseEntity<>(airplanes, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Customer>> getCustomers() {
        Set<Customer> customers = customerService.getCustomers();
        return customers != null ? new ResponseEntity<>(customers, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Get -> getCustomersbyEmail*/
    @GetMapping(value = "/customer/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        return customer != null ? new ResponseEntity<>(customer, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*Get Endpoint -> getFights*/
    @GetMapping(value = "/flights", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Flight>> getFlights() {
        Set<Flight> flights = flightService.getFlights();
        return flights != null ? new ResponseEntity<>(flights, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Get Endpoint -> getFlightByFlightId*/
    @GetMapping(value = "/flight/{flightId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flight> getFlightByFlightId(@PathVariable Integer flightId) {
        try {
            Flight flight = null;
            if(Util.validateNumber(flightId)) {
                flight = flightService.getFlightById(flightId);
            }
            return flight != null ? new ResponseEntity<>(flight, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Get Endpoint -> getFlightsForToday*/
   @GetMapping(value = "/flights/today", produces=MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Set<Flight>> getFlightsForToday() {
       Set<Flight> flights = flightService.getFlightsForToday();
       return flights != null ? new ResponseEntity<>(flights, HttpStatus.OK) :
               new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   /*Get Endpoint -> getFlightsByDate*/
    @GetMapping(value = "/flights/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Flight>> getFlightsById(@PathVariable String date) {
        try {
            Set<Flight> flights = flightService.getFlightByDate(Util.stringDateToDateTime(date));
            return flights != null ? new ResponseEntity<>(flights, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Get Endpoint -> getFlightsByFare*/
    @GetMapping(value = "/flights/fare/{fare}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Flight>> getFlightsByFare(@PathVariable Float fare) {
        Set<Flight> flights = flightService.getFlightByFare(fare);
        return flights != null && !flights.isEmpty() ? new ResponseEntity<>(flights, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Get Endpoint -> getFlightsByStatus*/
    @GetMapping(value = "/flights/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Flight>> getFlightsByStatus(@PathVariable String status) {
        try {
            Set<Flight> flights = flightService.getFlightByStats(status);
            return flights != null ? new ResponseEntity<>(flights, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Get Endpoint - > getAllRSVPsByCustomerId*/
    @GetMapping(value ="/rsvps/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Reservation>> getAllRSVPsByCustomerId(@PathVariable Integer customerId) {
        try {
            Set<Reservation> reservations = null;
            if(Util.validateNumber(customerId)) {
                reservations = reservationService.getAllRSVPsByCustomerId(customerId);
            }
            return reservations != null && !reservations.isEmpty() ? new ResponseEntity<>(reservations, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Get Endpoint -> getAllCancelledRSVPs*/
    @GetMapping(value = "/rsvps/cancelled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Reservation>> getAllCancelledRSVPs() {
        Set<Reservation> reservations = reservationService.getAllCancelledRSVPs();
        return reservations != null ? new ResponseEntity<>(reservations, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Get Endpoint -> getAllActiveRSVPsByAirline*/
    @GetMapping(value = "/rsvps/{airline}/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Reservation>> getAllActiveRSVPsByAirline(@PathVariable String airline) {
        try {
            Set<Reservation> reservations = reservationService.getAllActiveRSVPsByAirline(airline);
            return reservations != null ? new ResponseEntity<>(reservations, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Get Endpoint -> getAllCancelledRSVPsByAirline*/
    @GetMapping(value = "/rsvps/{airline}/cancelled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Reservation>> getAllCancelledRSVPsByAirline(@PathVariable String airline) {
        try {
            Set<Reservation> reservations = reservationService.getAllCancelledRSVPsByAirline(airline);
            return reservations != null ? new ResponseEntity<>(reservations, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /*Post Endpoint ->  insertFlight*/
    @PostMapping(value = "/flight", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flight> insertFlight(@RequestBody Flight flight) {
        try {
            return new ResponseEntity<>(flightService.addFlight(flight), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /*Post Endpoint -> addRSVPByCustomerId*/
    @PostMapping(value = "/rsvp/customer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addRSVPByCustomerId(@RequestBody Map<String, Object> json) {
        try {
            return reservationService.addRSVPByCustomerId(json) ? new ResponseEntity<>(true, HttpStatus.OK) :
                    new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            InvalidRequestException response = new InvalidRequestException(HttpStatus.CHECKPOINT.value(), ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CHECKPOINT);
        } catch (InvalidRequestException ex) {
            InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(ex);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException ex) {
            InvalidRequestExceptionResponse response = new InvalidRequestExceptionResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /*Post Endpoint -> addAirport*/
    @PostMapping(value = "/airport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airport> addAirport(@RequestBody Airport airport) {
        Airport addedAirport = airportService.addAirport(airport);
        return addedAirport != null ? new ResponseEntity<>(airport, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*Put Endpoibt - > cancelRSVPByCustomerId*/
    @PutMapping(value = "/rsvp/cancel/{rsvpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> cancelRSVPByCustomerId(@PathVariable Integer rsvpId) {
        try {
            boolean isCancelled = reservationService.cancelRSVPByCustomerId(rsvpId);
            return isCancelled ? new ResponseEntity<>(true, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /*Put Endpoint -> cancelFlightById*/
    @PutMapping(value = "/flight/cancel/{flightId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> cancelFlightById(@PathVariable Integer flightId) {
        return flightService.cancelFlight(flightId) ? new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
    }

    /*Delete Endpoint -> deleteCustomerById*/
    @DeleteMapping(value = "/delete/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteCustomerById(@PathVariable Integer customerId) {
        try {
            return customerService.deleteCustomerById(customerId) ? new ResponseEntity<>(true, HttpStatus.OK) :
                    new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), ex);
        }
    }
}
