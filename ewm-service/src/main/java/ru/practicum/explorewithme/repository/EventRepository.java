package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {
    @Query(value = "select e from Event e")
    List<Event> findAllFrom(Limit size);

    Event findByIdAndInitiatorId(Integer id, Integer initiatorId);

    List<Event> findByInitiatorId(Integer initiatorId, PageRequest pageRequest);

    List<Event> findByCategoryIdIn(List<Integer> categoryIds);
}
