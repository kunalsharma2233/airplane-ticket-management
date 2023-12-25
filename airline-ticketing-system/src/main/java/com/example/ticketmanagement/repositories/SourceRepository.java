package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Source;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends CrudRepository<Source, Integer> {
}
