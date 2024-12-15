package org.example.repository;

import org.example.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LocationRepository extends JpaRepository<Location, Integer>, QuerydslPredicateExecutor<Location> {
}
