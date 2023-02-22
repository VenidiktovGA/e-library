package ru.venidiktov.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.venidiktov.repo.BookRepoJpa;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BookServiceTest {
    @Mock
    BookRepoJpa bookRepoJpa;
    @Mock
    PersonService personService;

    @BeforeAll
    static void beforeAllTest() {
        System.out.println("Before all test");
    }

    @BeforeEach
    void dependenciesNotNull() {
        System.out.println("Before test this = " + this);
        assertNotNull(bookRepoJpa, "BookRepoJpa не должно быть null");
        assertNotNull(personService, "PersonService не должно быть null");
    }

    @Test
    void bookByNameNotFound() {
        System.out.println("Test1 this = " + this);
        when(bookRepoJpa.findBookLikeName(anyString())).thenReturn(Collections.EMPTY_LIST);
        var bookService = new BookService(bookRepoJpa, personService);
        assertEquals(bookService.getBookLikeName("Book name").size(), 0, "Book list should be empty");
    }

    @Test
    void bookByIdNotFound() {
        System.out.println("Test2 this = " + this);
        when(bookRepoJpa.findById(anyInt())).thenReturn(Optional.empty());
        var bookService = new BookService(bookRepoJpa, personService);
        assertThrows(RuntimeException.class, () -> bookService.getBookById(22), "Book should be not found");
    }
}