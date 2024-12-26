package ru.practicum.explorewithme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compilations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "pinned")
    private Boolean pinned;
}
