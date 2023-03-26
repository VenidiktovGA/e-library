package ru.venidiktov.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import ru.venidiktov.model.Users;
import ru.venidiktov.repo.UsersRepo;

import java.util.Optional;

/**
 * (Будущему себе)Да я уже умею поднимать testContainers и с ним все тестировать, но цель изучить Mockito
 **/
@ExtendWith(MockitoExtension.class)
class UsersValidatorTest {

    @Mock
    UsersRepo usersRepo;

    @Mock
    BeanPropertyBindingResult errors;

    UsersValidator usersValidator;

    @BeforeEach
    public void init() {
        usersValidator = new UsersValidator(usersRepo);
    }

    @Test
    void validate_shouldValid_ifPersonWithCurrentNameNotExist() {
        Mockito.doReturn(Optional.empty()).when(usersRepo).findByUsername(Mockito.anyString());
        usersValidator.validate(new Users("name", 25), errors);

        Mockito.verify(errors, Mockito.never()).rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void validate_shouldErrorValid_ifPersonWithCurrentNameExist() {
        Mockito.doReturn(Optional.of(new Users())).when(usersRepo).findByUsername(Mockito.anyString());
        usersValidator.validate(new Users("name", 25), errors);

        Mockito.verify(errors).rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}