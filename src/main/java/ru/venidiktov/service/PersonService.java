package ru.venidiktov.service;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.PersonRepoJpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepoJpa personRepo;

    public PersonService(PersonRepoJpa personRepo) {
        this.personRepo = personRepo;
    }

    public void resolveNplusOneProblem() {
        List<Person> persons = personRepo.resolveNplusOneProblem();
        System.out.println("resolve N + 1 problem");
    }

    public List<Person> getAllPerson() {
        return personRepo.findAll();
    }

    public Page<Person> getPagePerson(Integer page) {
        if (page == null) {
            page = 0;
        }
        return personRepo.findAll(PageRequest.of(page, 10));
    }

    @Transactional
    public void createPerson(Person person) {
        personRepo.save(person);
    }

    public Person getPersonById(int id) {
        Person person = personRepo
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Человек c id = " + id + " не найден!"));
        return person;
    }

    public Person getPersonByIdWithBook(int id) {
        Person person = getPersonById(id);
        Hibernate.initialize(person.getBookList());
        return person;
    }

    public Map<Book, Integer> getBookWithExpiration(List<Book> books) {
        return books.stream()
                .collect(Collectors
                        .toMap(
                                e -> e,
                                e -> {
                                    if (e.getAssignDate() == null) return 0;
                                    else return e.getAssignDate().plusDays(10L).compareTo(LocalDate.now());
                                }
                        )
                );
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
