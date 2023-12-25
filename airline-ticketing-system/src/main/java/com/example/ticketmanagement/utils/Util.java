package com.example.ticketmanagement.utils;

import com.example.ticketmanagement.errors.InvalidRequestException;
import com.example.ticketmanagement.models.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class Util {
    public static final String JSON_PATTERN = "MM-dd-yyyy HH:mm:ss";
    public static final String DB_TO_VIEW = "MM-dd-yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";

    //custom keys for Json
    public static final String RSVP_ID_JKEY = "reservation_id";
    public static final String FLIGHT_ID_JKEY = "flightId";
    public static final String CUSTOMER_ID_JKEY = "customerId";
    public static final String DEPARTURE_DATE_TIME_JKEY = "departureDateTime";
    public static final String ARRIVAL_DATE_TIME_JKEY = "arrivalDateTime";

    public static boolean verifyRSVPByCustomerId(final Map<String, Object> json) throws InvalidRequestException {
        if(json.size() != 2)
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST.value(), "request must have 2 values, found = " + json.size());
        if(json.getOrDefault(CUSTOMER_ID_JKEY, -1).equals(-1))
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST.value(), "request body missing key=" + CUSTOMER_ID_JKEY);
        if(json.getOrDefault(FLIGHT_ID_JKEY, -1).equals(-1))
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST.value(), "request body missing key=" + FLIGHT_ID_JKEY);
        return true;
    }

    public static LocalDateTime toViewDateTime(LocalDateTime dbDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_TO_VIEW, Locale.US);
        String formatted = formatter.format(dbDateTime);
        return LocalDateTime.parse(formatted, formatter);
    }

    public static LocalDateTime toDBDateTime(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    public static boolean validateAirlineName(String airlineName) throws IllegalArgumentException {
        if(airlineName == null || airlineName.trim().toLowerCase().isEmpty())
            throw new IllegalArgumentException("Invalid airline name");
        return true;
    }

    public static boolean validateAirportName(String airportName) throws IllegalArgumentException {
        if(airportName == null || airportName.trim().toLowerCase().isEmpty())
            throw new IllegalArgumentException("Invalid airport name");
        return true;
    }

    public static boolean validateEmail(String email) throws IllegalArgumentException {
        if(email == null || email.trim().toLowerCase().isEmpty())
            throw new IllegalArgumentException("Invalid email");
        return true;
    }

    public static boolean validateNumber(Integer number) throws IllegalArgumentException {
        if(number > 0.0)
            return true;
        else throw new IllegalArgumentException("Invalid Number Provided");
    }

    public static boolean validateNumber(Float number) throws IllegalArgumentException {
        if(number > 0.0)
            return true;
        else throw new IllegalArgumentException("Invalid Number Provided");
    }

    public static LocalDateTime stringDateToDateTime(String dateTime) throws IllegalArgumentException {
        try {
            if(dateTime.trim().isEmpty() || dateTime.length() < 10)
                throw new IllegalArgumentException("Invalid date. format=" + DATE_FORMAT);

            String[] parts = dateTime.split(" ");
            if(parts.length < 1)
                throw new IllegalArgumentException("Invalid date format. format = " + DATE_FORMAT);
            if(parts.length == 1) {
                String[] dateParts = parts[0].split("-");
                if(dateParts.length < 3)
                    throw new IllegalArgumentException("Invalid date format. format = " + DATE_FORMAT);
                return LocalDateTime.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[0]),
                        Integer.parseInt(dateParts[1]), 0 , 0, 0, 0);
            } else if(parts.length == 2) {
                String[] dateParts = parts[0].split("-");
                String[] timeParts = parts[1].split(":");

                if(dateParts.length < 3 || timeParts.length < 3)
                    throw new IllegalArgumentException("Invalid date time format. format="+JSON_PATTERN);
                return LocalDateTime.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[0]),
                        Integer.parseInt(dateParts[1]), Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]),
                        Integer.parseInt(dateParts[2]));
            }
            else throw new IllegalArgumentException("Invalid date/datetime given. format=" + JSON_PATTERN);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    public static Status validateFlightStatus(String status) throws IllegalArgumentException {
        if(status.trim().equalsIgnoreCase(Status.ON_TIME.toString()))
            return Status.ON_TIME;
        else if(status.trim().equalsIgnoreCase(Status.CANCELLED.toString()))
            return Status.CANCELLED;
        else throw new IllegalArgumentException("Invalid status provided. status=ON_TIME or CANCELLED");
    }

    public static boolean validateAirport(Airport airport) throws IllegalArgumentException {
        if(airport == null)
            throw new IllegalArgumentException("Null Argument provided");
        if(airport.getAirportId() == null || airport.getAirportId() <= 0)
            airport.setAirportId(null);
        if(airport.getAirportName().trim().toLowerCase().length() < 2)
            throw new IllegalArgumentException("Airport name is too short, must atleast of length 2");
        return true;
    }

    public static boolean validateFlight(Flight flight) throws IllegalArgumentException {
        if(flight == null)
            throw new NullPointerException("Flight object is null");
        if(flight.getFlightId() == null || flight.getFlightId() <= 0) {
            flight.setFlightId(null);
            throw new IllegalArgumentException("Flight id must not be null or zero");
        }
        if(flight.getFlightCode() == null ||
                flight.getFlightCode().trim().isEmpty() ||
                flight.getFlightCode().trim().length() < 1)
            throw new IllegalArgumentException("Invalid flight code provided.");
        if(validateSource(flight.getSource()) && validateDestination(flight.getDestination()) && validateAirplane(flight.getAirplane())) {
            if(hasAirportLoop(flight.getSource(), flight.getDestination()))
                throw new IllegalArgumentException("Airport are same.");
            if(!isArrivalAfterDeparture(flight.getSource().getDepartureDateTime(), flight.getDestination().getArrivalDateTime()))
                throw new IllegalArgumentException("Arrival time is same or before departure time");
            if(flight.getFare() == null || flight.getFare() <= 0) throw new IllegalArgumentException("Invalid fare.");
            if(flight.getCapacity()  == null || flight.getCapacity() <= 0) throw new IllegalArgumentException("Invalid capacity.");
            return true;
        }
        return false;
    }

    public static boolean validateAirplane(Airplane airplane) throws IllegalArgumentException {
        if(airplane == null) throw new NullPointerException("Null argument provided ");
        if(airplane.getAirplaneId() == null || airplane.getAirplaneId() <= 0)
            airplane.setAirplaneId(null);
        if(airplane.getAirplaneName() == null)
            throw new IllegalArgumentException("Airplane cannot be null ");
        if(airplane.getAirplaneName().trim().toLowerCase().length() < 2)
            throw new IllegalArgumentException("Aiplane name is too short, must be atleast 2 ");
        return validateAirline(airplane.getAirline());
    }

    public static boolean validateAirline(Airline airline) throws IllegalArgumentException {
        if(airline == null)
            throw new NullPointerException("Airplane provided null ");
        if(airline.getAirlineId() == null || airline.getAirlineId() <= 0)
            airline.setAirlineId(null);
        if(airline.getAirlineName() == null)
            throw new IllegalArgumentException("Airline name cannot be null ");
        if(airline.getAirlineName().trim().toLowerCase().length() < 2)
            throw new IllegalArgumentException("Airline name must be atleast 2 ");
        return true;
    }

    public static boolean validateSource(Source source) throws IllegalArgumentException {
        if(source == null)
            throw new NullPointerException("Source provided null ");
        if(source.getSourceId() == null || source.getSourceId() <= 0)
            source.setSourceId(null);
        if(source.getDepartureDateTime() == null)
            throw new IllegalArgumentException("Invalid departure time");
        return validateAirport(source.getAirport());
    }

    public static boolean validateDestination(Destination destination) throws IllegalArgumentException {
        if(destination  == null)
            throw new NullPointerException("Destination provided null ");
        if(destination.getDestinationId() == null || destination.getDestinationId() <= 0)
            destination.setDestinationId(null);
        if(destination.getArrivalDateTime() == null)
            throw new IllegalArgumentException("Invalid Arrival Time");
        return validateAirport(destination.getAirport());
    }

    public static boolean hasAirportLoop(Source source, Destination destination) {
        return source.getAirport().getAirportName().trim().equalsIgnoreCase(destination.getAirport().getAirportName());
    }

    public static boolean isArrivalAfterDeparture(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return arrivalDateTime.isAfter(departureDateTime);
    }

}
