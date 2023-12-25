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
@EqualsAndHashCode
@Builder
@Data
@Entity
@Table(name="sources")
public class Source {

    @Id
    @Column(name = "source_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sourceId;

    @Column(name = "date_time")
    @JsonFormat(pattern = "MM-dd-yyyy:HH:mm:ss")
    private LocalDateTime departureDateTime;

    @JoinColumn(name = "airport_id", referencedColumnName = "airport_id")
    @OneToOne
    private Airport airport;

}
