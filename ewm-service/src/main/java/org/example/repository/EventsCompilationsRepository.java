package org.example.repository;

import org.example.model.EventsCompilations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EventsCompilationsRepository extends JpaRepository<EventsCompilations, Integer>,
        QuerydslPredicateExecutor<EventsCompilations> {

    @Query(value = "select ec.eventId from EventsCompilations ec " +
            "where ec.compilationId in (?1) group by ec.eventId")
    List<Integer> findByCompilationId(Integer compId);

    void deleteByCompilationId(Integer compId);
}
