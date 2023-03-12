package ru.venidiktov.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.venidiktov.BaseTest;
import ru.venidiktov.model.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

class PersonServiceTest extends BaseTest {

    @ParameterizedTest
    @CsvSource({
            "1,Ivanov,Ivan,Ivanovitch,1960-01-01",
            "2,Petrov,Petr,Petrovitch,1960-01-01"
    })
    void getPersonById_ShouldFindPerson_ifPersonExists(
            Integer id, String surname, String name, String middleName, String birthday
    ) {
        Person actualPerson = personService.getPersonById(id);

        assertAll(
                () -> assertThat(actualPerson).isNotNull(),
                () -> assertThat(actualPerson.getSurname()).isEqualToIgnoringCase(surname),
                () -> assertThat(actualPerson.getName()).isEqualToIgnoringCase(name),
                () -> assertThat(actualPerson.getMiddleName()).isEqualToIgnoringCase(middleName),
                () -> assertThat(actualPerson.getBirthday()).isEqualTo(birthday)
        );
        verify(personRepo).findById(1);
    }
}