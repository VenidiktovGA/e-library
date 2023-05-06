package ru.venidiktov.validator;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import ru.venidiktov.model.Users;
import ru.venidiktov.repo.UsersRepo;

/**
 * (Будущему себе)Да я уже умею поднимать testContainers и с ним все тестировать, но цель изучить Mockito
 **/
@ExtendWith(MockitoExtension.class)
class UsersValidatorTest {

    @Captor
    private ArgumentCaptor<String> argumentCaptor;

    @Mock
    UsersRepo usersRepo;

    @Mock
    BeanPropertyBindingResult errors;

    @InjectMocks
    UsersValidator usersValidator;

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

        verify(usersRepo).findByUsername(argumentCaptor.capture());
        Mockito.verify(errors).rejectValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        assertThat(argumentCaptor.getValue()).isEqualTo("name");
    }
}