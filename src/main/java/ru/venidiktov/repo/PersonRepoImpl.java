package ru.venidiktov.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.model.Person;

import java.util.List;

@Repository
public class PersonRepoImpl implements PersonRepo {

    private final SessionFactory sessionFactory;

    public PersonRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select p from Person p", Person.class).getResultList();
    }

    @Override
    @Transactional
    public Person getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Override
    @Transactional
    public void create(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(person);
    }

    @Override
    @Transactional
    public void update(Person person) {
        Session session = sessionFactory.getCurrentSession();
        Person personUpdated = session.get(Person.class, person.getId());
        //Объект personUpdated находится в состоянии manage
        personUpdated.setName(person.getName());
        personUpdated.setSurname(person.getSurname());
        personUpdated.setMiddleName(person.getMiddleName());
        personUpdated.setBirthday(person.getBirthday());
        session.persist(personUpdated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person deletedPerson = session.get(Person.class, id);
        session.remove(deletedPerson);
    }
}
