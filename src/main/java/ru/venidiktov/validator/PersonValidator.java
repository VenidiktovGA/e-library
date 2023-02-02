package ru.venidiktov.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.PersonRepoJpa;

/**
 * Полезная статья про валидацию https://habr.com/ru/post/424819/
 */
@Component
public class PersonValidator implements Validator {
    private final PersonRepoJpa personRepo;

    public PersonValidator(PersonRepoJpa personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personRepo.getByFullName(person) != null) {
            errors.rejectValue("name", "", "Читатель с таким ФИО уже есть");
        }
    }
}
