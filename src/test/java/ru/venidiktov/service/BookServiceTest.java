package ru.venidiktov.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.venidiktov.model.Book;
import ru.venidiktov.paramresolver.BookServiceParamResolver;
import ru.venidiktov.repo.BookRepoJpa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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
    @ValueSource(strings = "451 градус по фаренгейту")
    void getBookLikeName_NotFound_IfBookNotExist() {
        when(bookRepoJpa.findBookLikeName(anyString())).thenReturn(Collections.EMPTY_LIST);

        var expectedBook = bookService.getBookLikeName("Book name");

        assertThat(expectedBook).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void getBookLikeName_parametrizedTest(String bookName) {
        List<Book> findBook = bookService.getBookLikeName(bookName);

        assertThat(findBook).isNull();
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
                () -> assertThat(findingBook).isNotNull(),
                () -> assertThat(findingBook).isEqualTo(actualBook)
        );
        verify(bookRepoJpa).findById(1);
    }

    @ParameterizedTest(name = "Get page with page number = {index} - {0}, total elements = {index} - {1}")
    @MethodSource("getArgumentsForPageBookTest")
    void getPageBook_ParameterizedTest(Integer pageNumber, boolean desc, Page<Book> book) {
        when(bookRepoJpa.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "yearPublishing")))
                .thenReturn(new PageImpl<Book>(List.of(mock(Book.class))));

        Page<Book> findingBook = bookService.getPageBook(pageNumber, desc);

        System.out.println("fine");
    }

    static Stream<Arguments> getArgumentsForPageBookTest() {
        return Stream.of(
                Arguments.of(null, true, new PageImpl<Book>(List.of(mock(Book.class)))),
                Arguments.of(0, true, new PageImpl<Book>(List.of(mock(Book.class)))),
                Arguments.of(10, true, new PageImpl<Book>(List.of(mock(Book.class))))
        );
    }
}