package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events_compilations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventsCompilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "event_id")
    private Integer eventId;
    @Column(name = "compilation_id")
    private Integer compilationId;
}
