package com.example.ticketmanagement.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "airplanes")
public class Airplane {

    @Id
    @Column(name = "airplane_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer airplaneId;

    @Column(name = "airplane_name")
    private String airplaneName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "airline_id", name = "airline_id")
    private Airline airline;
}
