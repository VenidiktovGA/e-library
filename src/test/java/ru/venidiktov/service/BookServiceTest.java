package ru.venidiktov.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.venidiktov.model.Book;
import ru.venidiktov.paramresolver.BookServiceParamResolver;
import ru.venidiktov.repo.BookRepoJpa;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({
        MockitoExtension.class,
        BookServiceParamResolver.class
})
@DisplayName("Test book service")
class BookServiceTest {
    @Mock
    private BookRepoJpa bookRepoJpa;
    @Mock
    private PersonService personService;
    @InjectMocks
    private BookService bookService;

    BookServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    static void myResolverExample(Book book) {
        System.out.println(book);
    }

    @BeforeEach
    void dependenciesShouldBeNotNull() {
        assertNotNull(bookRepoJpa, "BookRepoJpa не должно быть null");
        assertNotNull(personService, "PersonService не должно быть null");
    }

    @Test
    @Tag("notFoundBook")
    void getBookLikeName_NotFound_IfBookNotExist() {
        when(bookRepoJpa.findBookLikeName(anyString())).thenReturn(Collections.EMPTY_LIST);

        var expectedBook = bookService.getBookLikeName("Book name");

        Assertions.assertThat(expectedBook).isEmpty();
    }

    @Test
    @Tag("notFoundBook")
    void getBookById_ThrowRuntimeException_IfBookNotExist() {
        when(bookRepoJpa.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bookService.getBookById(22);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @Tag("foundBook")
    void getBookById_ReturnFindingBook_IfBookExist() {
        Book findingBook = mock(Book.class);
        when(bookRepoJpa.findById(1)).thenReturn(Optional.ofNullable(findingBook));

        final Book actualBook = bookService.getBookById(1);

        assertAll(
                () -> Assertions.assertThat(findingBook).isNotNull(),
                () -> Assertions.assertThat(findingBook).isEqualTo(actualBook)
        );
        verify(bookRepoJpa).findById(1);
    }
}