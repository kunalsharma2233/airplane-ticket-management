package com.example.ticketmanagement.passengers;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {

        private PassengerService passengerService;

        public PassengerController(PassengerService passengerService) {
            this.passengerService = passengerService;
        }

        @RequestMapping(value = "/test",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public String testPassenger() {
            return "Running fine";
        }

        /*
        *   Creating POST request to save passengers who register
        *
        * */
        @RequestMapping(value = "/create-passenger",
                method = RequestMethod.POST,
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Passengers> createPassenger(@Valid @RequestBody Passengers passengers) {
            boolean flag = passengerService.savePassenger(passengers);
            return flag == true ? new ResponseEntity<>(HttpStatus.CREATED) :
                    new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        @RequestMapping(value = "/passenger-list",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public ResponseEntity<List<Passengers>> getAllPassengers() {
            List<Passengers> passengers = passengerService.getPassengerList();
            return passengers != null ? new ResponseEntity<>(passengers, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @RequestMapping(value = "/get-passenger/{passengerId}",
                method = RequestMethod.GET,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public EntityModel<Passengers> getPassenger(@PathVariable Integer passengerId) {
            Optional<Passengers> passenger = passengerService.getPassenger(passengerId);
            if(passenger.isEmpty())
                throw new PassengerNotFoundException(" passengerId " +passengerId);
            EntityModel<Passengers> entityModel = EntityModel.of(passenger.get());
            WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.
                    methodOn(this.getClass()).getAllPassengers());
            entityModel.add(link.withRel("allPassengerList"));
            return entityModel;
        }




}
