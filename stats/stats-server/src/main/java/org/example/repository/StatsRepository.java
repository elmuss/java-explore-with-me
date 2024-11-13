package org.example.repository;

import org.example.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {
    @Query(value = "select s from Stats s where s.uri like (?1) and s.timestamp between ?2 and ?3")
    List<Stats> findByUriAndTimestamp(String uri, Instant start, Instant end);

    @Query(value = "select s from Stats s where s.uri like (?1) and s.timestamp between ?2 and ?3")
    List<Stats> findByUriAndTimestampUnique(String uri, Instant start, Instant end);

    @Query(value = "select s from Stats s where s.timestamp between ?1 and ?2")
    List<Stats> findAllByTimestamp(Instant start, Instant end);

    @Query(value = "select s from Stats s where s.timestamp between ?1 and ?2")
    List<Stats> findAllByTimestampUnique(Instant start, Instant end);
}
