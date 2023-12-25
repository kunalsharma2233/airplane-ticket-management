package com.example.ticketmanagement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @Column(name = "destination_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer destinationId;

    @Column(name = "date_time")
    @JsonFormat(pattern = "DD-mm-yyyy:HH:mm:ss")
    private LocalDateTime arrivalDateTime;

    @OneToOne
    @JoinColumn(name = "airport_id", referencedColumnName = "airport_id")
    private Airport airport;

}
