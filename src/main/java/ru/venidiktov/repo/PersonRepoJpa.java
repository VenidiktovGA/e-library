package ru.venidiktov.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.venidiktov.model.Person;

import java.util.List;

@Repository
public interface PersonRepoJpa extends JpaRepository<Person, Integer> {
    @Modifying
    @Query("select p from Person p LEFT JOIN FETCH p.bookList")
    List<Person> resolveNplusOneProblem();
}
