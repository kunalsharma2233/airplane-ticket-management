package com.example.ticketmanagement.services;/*
 *  Created by
 *   Name : kunal
 *   Date : 24-12-2023
 *   Project Name : airline-ticketing-system
 * */

import com.example.ticketmanagement.models.Source;
import com.example.ticketmanagement.repositories.SourceRepository;
import com.example.ticketmanagement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class SourceService {
    private final SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public Source getSourceById(Integer sourceId) throws IllegalArgumentException {
        if(Util.validateNumber(sourceId)) {
            Optional<Source> source = sourceRepository.findById(sourceId);
            return source.isPresent() ? source.get() : null;
        }
        return null;
    }

    public Source addSource(Source source) throws IllegalArgumentException {
        if(Util.validateSource(source)) {
            return sourceRepository.save(source);
        }
        return null;
    }
}
