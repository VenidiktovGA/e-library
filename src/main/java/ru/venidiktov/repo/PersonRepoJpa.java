package ru.venidiktov.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.venidiktov.model.Person;

@Repository
public interface PersonRepoJpa extends JpaRepository<Person, Integer> {
}
