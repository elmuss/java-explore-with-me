package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {
    @Query(value = "select u from User u where id in (?1)")
    List<User> getUsersByIds(List<Integer> ids);
}
