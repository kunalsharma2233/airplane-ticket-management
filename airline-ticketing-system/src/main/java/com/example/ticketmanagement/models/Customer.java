package com.example.ticketmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "customers")
    private Set<Flight> flights;

    @JsonIgnore
    @OneToMany(mappedBy = "customers")
    private Set<Reservation> reservations;

}
