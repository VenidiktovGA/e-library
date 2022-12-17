package ru.venidiktov.repo;

import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.model.Person;

import java.util.List;

public interface PersonRepo {
    @Transactional(readOnly = true)
    List<Person> getAll();

    @Transactional
    Person getById(int id);

    @Transactional
    void create(Person person);

    @Transactional
    void update(Person person);

    @Transactional
    void deleteById(int id);
}
