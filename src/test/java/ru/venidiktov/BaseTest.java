package ru.venidiktov;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.venidiktov.repo.BookRepoJpa;
import ru.venidiktov.repo.PersonRepoJpa;
import ru.venidiktov.service.BookService;
import ru.venidiktov.service.PersonService;

@DataJpaTest
@Testcontainers
@Sql(value = {"/schema.sql", "/data.sql"})
@Import({BookService.class, PersonService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseTest {

    @SpyBean
    public BookRepoJpa bookRepo;

    @SpyBean
    public PersonRepoJpa personRepo;

    @SpyBean
    public BookService bookService;

    @SpyBean
    public PersonService personService;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3");
//            .withDatabaseName("eLibrary")
//            .withUsername("postgres")
//            .withPassword("qwerty");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
