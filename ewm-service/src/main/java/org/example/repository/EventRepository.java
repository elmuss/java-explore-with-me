package org.example.repository;

import org.example.model.Event;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {
    @Query(value = "select e from Event e")
    List<Event> findAllFrom(Limit size);

    Event findByIdAndInitiatorId(Integer id, Integer initiatorId);

    List<Event> findByInitiatorId(Integer initiatorId);

    List<Event> findByCategoryIdIn(List<Integer> categoryIds);
}
