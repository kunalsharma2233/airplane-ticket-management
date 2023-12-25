package com.example.ticketmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Data
@Entity
@Table(name = "airlines")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airline implements Serializable {

    @Id
    @Column(name = "airline_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer airlineId;

    @Column(name = "airline_name")
    private String airlineName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "airline")
    private Set<Airplane> airplanes;
}
