package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import ru.venidiktov.dao.PersonDao;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.PersonRepo;

import java.util.List;

@Service
public class PersonService {

    private final PersonDao personDao;

    private final PersonRepo personRepo;

    public PersonService(PersonDao personDao, PersonRepo personRepo) {
        this.personDao = personDao;
        this.personRepo = personRepo;
    }

    public List<Person> getAllPerson() {
        return personRepo.getAll();
    }

    public List<Person> getAllPersonWithoutBook() {
        return personDao.getAllWithoutBook();
    }

    public void createPerson(Person person) {
        personRepo.create(person);
    }

    public Person getPersonById(int id) {
        return personRepo.getById(id);
    }

    public void updatePersonById(int id, Person person) {
        person.setId(id);
        personRepo.update(person);
    }

    public void deletePersonById(int id) {
        personRepo.deleteById(id);
    }
}
