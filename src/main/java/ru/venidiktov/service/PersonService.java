package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import ru.venidiktov.dao.PersonDao;
import ru.venidiktov.model.Person;

import java.util.List;

@Service
public class PersonService {

    private final PersonDao personDao;

    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<Person> getAllPerson() {
        return personDao.getAll();
    }

    public List<Person> getAllPersonWithoutBook() {
        return personDao.getAllWithoutBook();
    }

    public void createPerson(Person person) {
        personDao.create(person);
    }

    public Person getPersonById(int id) {
        return personDao.getById(id);
    }

    public void updatePersonById(int id, Person person) {
        person.setId(id);
        personDao.update(person);
    }

    public void deletePersonById(int id) {
        personDao.deleteById(id);
    }
}
