package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "lat", nullable = false)
    private Double lat;
    @Column(name = "lon", nullable = false)
    private Double lon;
}
