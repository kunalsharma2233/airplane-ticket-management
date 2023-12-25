package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Destination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends CrudRepository<Destination, Integer> {
}
