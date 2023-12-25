package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Reservation;
import com.example.ticketmanagement.models.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Query(value = "select DISTINCT from reservations r where r.customer_id = :customer_id")
    Set<Reservation> findAllRSVPByCustomerId(@Param("customer_id") Integer customerId);

    Set<Reservation> findReservationByStatus(Status status);

    @Query(value = "select * from reservations r inner join flights f on f.flight_id in " +
            "(select flight_id from flights where airplane_id in " +
            "(select ap.airplane_id from airplanes ap inner join airlines al " +
            "on al.airline_id = (select airline_id  from airlines where lower(airline_name) = lower(:airline)) " +
            "and al.airline_id = ap.airline_id )) and r.flight_id = f.flight_id and lower(r.status) = lower(:status)", nativeQuery = true)
    Set<Reservation> findActiveReservationsByAirline(@Param("airline") String airline,
                                                     @Param("status") String status);

    @Query(value = "insert into reservations (date_time, status, customer_id, flight_id) " +
                    "values(:date_time, :status, :customer, :flight_id)", nativeQuery = true)
    void insertRSVPByCustomerId(@Param("date_time")LocalDateTime dateTime, @Param("status") String status,
                                @Param("customer_id")int customerId, @Param("flight_id")int flightId);
}
