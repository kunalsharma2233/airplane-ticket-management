package com.example.ticketmanagement.passengers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "passenger_details")
@ToString
@EqualsAndHashCode
public class Passengers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerId;
    @Column(name = "name")
    @Size(min = 6, max = 15)
    private String passengerName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    @JsonIgnore
    private String password;

}
