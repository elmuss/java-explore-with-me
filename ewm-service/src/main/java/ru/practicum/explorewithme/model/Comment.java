package ru.practicum.explorewithme.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "event_id")
    private Integer eventId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "creation_date")
    private Instant created;
}
