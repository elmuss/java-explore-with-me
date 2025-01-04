package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, QuerydslPredicateExecutor<Comment> {
    List<Comment> findByEventId(Integer eventId);

    @Query(value = "select c from Comment c where eventId in (?1)")
    List<Comment> findByEventIds(List<Integer> eventIds);

    List<Comment> findByAuthorId(Integer authorId);
}
