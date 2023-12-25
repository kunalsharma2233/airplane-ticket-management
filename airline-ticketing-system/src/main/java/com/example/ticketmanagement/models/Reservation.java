package com.example.ticketmanagement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rspvId;

    @Column(name = "date_time")
    @JsonFormat(pattern = "MM-dd-yyyy:HH:mm:ss")
    private LocalDateTime rsvpDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
    private Flight flight;

    @JsonIgnore
    @ManyToMany
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
}
