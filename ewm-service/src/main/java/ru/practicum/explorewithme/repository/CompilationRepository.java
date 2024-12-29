package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>, QuerydslPredicateExecutor<Compilation> {
}
