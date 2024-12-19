package org.example.repository;

import org.example.model.Request;
import org.example.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer>, QuerydslPredicateExecutor<Request> {
    Optional<Request> getByRequesterIdAndEventId(Integer userId, Integer eventId);

    List<Request> findByEventId(Integer eventId);

    List<Request> findByRequesterId(Integer requesterId);

    Request findByIdAndRequesterId(Integer id, Integer requesterId);

    List<Request> findByEventIdAndStatusLike(Integer eventId, State state);
}
