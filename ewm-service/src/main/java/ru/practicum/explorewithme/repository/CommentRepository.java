package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, QuerydslPredicateExecutor<Comment> {
    List<Comment> findByEventId(Integer eventId);

    List<Comment> findByEventIdIn(List<Integer> eventIds);

    List<Comment> findByAuthorId(Integer authorId);
}
