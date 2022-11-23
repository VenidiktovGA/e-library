package ru.venidiktov.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.venidiktov.dao.PersonDao;
import ru.venidiktov.model.Person;

/**
 * Полезная статья про валидацию https://habr.com/ru/post/424819/
 */
@Component
public class PersonValidator implements Validator {

    private final PersonDao personDao;

    public PersonValidator(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personDao.getByFullName(person).isPresent()) {
            errors.rejectValue("name", "", "Читатель с таким ФИО уже есть");
        }
    }
}
