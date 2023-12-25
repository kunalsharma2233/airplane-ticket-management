package com.example.ticketmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @Column(name = "flight_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flightId;

    @Column(name = "flight_code", nullable = false)
    private String flightCode;

    @JoinColumn(referencedColumnName = "source_id", name = "source_id")
    @OneToOne
    private Source source;

    @JoinColumn(referencedColumnName = "destination_id", name = "destination_id")
    @OneToOne
    private Destination destination;

    @OneToOne
    @JoinColumn(referencedColumnName = "airplane_id", name = "airplane_id")
    private Airplane airplane;

    @Transient
    private Integer availableSeat;

    @Column(name = "fare")
    private Float fare;

    @Column(name = "capacity", updatable = false, nullable = false)
    private Integer capacity;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "customer_flights",
            inverseJoinColumns = {@JoinColumn(name = "customer_id", referencedColumnName = "customer_id")},
            joinColumns = {@JoinColumn(name = "flight_id", referencedColumnName = "flight_id")}
    )
    private Set<Customer> customers;

}
