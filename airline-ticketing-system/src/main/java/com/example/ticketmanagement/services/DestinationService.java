package com.example.ticketmanagement.services;/*
 *  Created by
 *   Name : kunal
 *   Date : 24-12-2023
 *   Project Name : airline-ticketing-system
 * */

import com.example.ticketmanagement.models.Destination;
import com.example.ticketmanagement.repositories.DestinationRepository;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DestinationService
{
    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public Destination getDestinationById(Integer destinationId) throws IllegalArgumentException {
        if(Util.validateNumber(destinationId)) {
            Optional<Destination> destination = destinationRepository.findById(destinationId);
            return destination.isPresent() ? destination.get() : null;
        }
        return null;
    }

    public Destination addDestination(Destination destination) throws IllegalArgumentException {
        if(Util.validateDestination(destination)) {
            return destinationRepository.save(destination);
        }
        return null;
    }
}
