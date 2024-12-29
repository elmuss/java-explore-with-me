package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>, QuerydslPredicateExecutor<Location> {
}
