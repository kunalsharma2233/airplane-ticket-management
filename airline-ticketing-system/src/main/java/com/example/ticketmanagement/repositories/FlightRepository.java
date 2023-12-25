package com.example.ticketmanagement.repositories;

import com.example.ticketmanagement.models.Flight;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Integer> {

    @Query(value = "select * from flights f inner join sources s" +
            " on s.source_id = f.source_id and s.date_time >= :date_time1 and s.date_time < :date_time2", nativeQuery = true)
    Set<Flight> findByCurrentDateTime(@Param("date_time1") LocalDateTime dateTime1,
                          @Param("date_time2") LocalDateTime dateTime2);

    @Query(value = "select * from flights f inner join sources s " +
                    "on s.source_id = f.source_id and s.date_time >= :date_time", nativeQuery = true)
    Set<Flight> findByDate(@Param("date_time") LocalDateTime dateTime);


    @Query(value = "select * from flight f where f.fare >=0 and f.fare <= :fare", nativeQuery = true)
    Set<Flight> findByFare(@Param("fare") Float fare);

    Set<Flight> findFlightsByStatus(@Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "delete from customers_flights cf where cf.flight_id = :flight_id ; " +
                    "update flights f set f.status = :_status where flight_id = :flight_id" +
                    "update reservations r set r.status = _status where  r.status = 'ACTIVE' and flight_id = :flight_id",
                    nativeQuery = true)
    void deleteCustomerRSVPByFlightId(@Param("flight_id") Integer flightId,
                                      @Param("_status") String flightStatus);

    @Modifying
    @Transactional
    @Query(value = "update flights f set f.status = :_status where f.flight_id = :flight_id", nativeQuery = true)
    void changeFlightStatus(@Param("_status") String status, @Param("flight_id") Integer flightId);
}
