package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.Stats;
import ru.practicum.explore.model.StatsCount;

import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    @Query(value = "select s.app, s.uri, count(s.ip) as hits from stats s where s.uri like (?1)" +
            "and s.timestamp between ?2 and ?3 group by s.app, s.uri", nativeQuery = true)
    StatsCount countH(String uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "select s.app, s.uri, count(distinct s.ip) as hits from stats s" +
            "where s.uri like (?1) and s.timestamp between ?2 and ?3" +
            "group by s.app, s.uri", nativeQuery = true)
    StatsCount countHU(String uri, LocalDateTime start, LocalDateTime end);
}
