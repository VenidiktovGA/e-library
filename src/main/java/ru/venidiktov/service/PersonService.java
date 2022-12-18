package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.dao.PersonDao;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.PersonRepoJpa;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonDao personDao;
    private final PersonRepoJpa personRepo;

    public PersonService(PersonDao personDao, PersonRepoJpa personRepo) {
        this.personDao = personDao;
        this.personRepo = personRepo;
    }

    public List<Person> getAllPerson() {
        return personRepo.findAll();
    }

    public List<Person> getAllPersonWithoutBook() {
        return personDao.getAllWithoutBook();
    }

    @Transactional
    public void createPerson(Person person) {
        personRepo.save(person);
    }

    public Person getPersonById(int id) {
        return personRepo.findById(id).orElseThrow(() -> new RuntimeException("Человек c id = " + id + " не найден!"));
    }

    @Transactional
    public void updatePersonById(int id, Person person) {
        person.setId(id);
        personRepo.save(person);
    }

    @Transactional
    public void deletePersonById(int id) {
        personRepo.deleteById(id);
    }
}
