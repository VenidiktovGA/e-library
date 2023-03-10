package ru.venidiktov.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.venidiktov.Application;
import ru.venidiktov.model.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@ContextConfiguration(classes = {Application.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/schema.sql", "/data.sql"})
public class BookRepoTest {

    @Autowired
    private BookRepoJpa bookRepo;

    @Autowired
    private PersonRepoJpa personRepo;

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

    @Test
    public void isExistsBook() {
        // TODO: 3/11/2023 Косяк так как подразумевается что имся будет в Upper 
        List<Book> books = bookRepo.findBookLikeName("КОЛЕЦ");
        assertEquals(1, books.size());

    }
}
