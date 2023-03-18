package ru.venidiktov.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.venidiktov.model.Users;
import ru.venidiktov.repo.UsersRepo;

@Component
public class UsersValidator implements Validator {

    private final UsersRepo usersRepo;

    public UsersValidator(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Users.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Users user = (Users) target;

        if (usersRepo.findByUsername(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Пользователь с таким именем уже есть!");
        }
    }
}
