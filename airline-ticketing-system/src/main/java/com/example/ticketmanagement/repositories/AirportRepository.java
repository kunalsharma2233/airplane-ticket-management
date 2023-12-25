package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Integer> {
    Airport findByAirportNameIgnoreCase(String airportName);
}
