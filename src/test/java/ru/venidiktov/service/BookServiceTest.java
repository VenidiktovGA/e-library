package ru.venidiktov.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Page;
import ru.venidiktov.BaseTest;
import ru.venidiktov.model.Book;
import ru.venidiktov.paramresolver.BookServiceParamResolver;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(BookServiceParamResolver.class)
@DisplayName("Test book service")
class BookServiceTest extends BaseTest {

    BookServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    static void myResolverExample(Book book) {
        System.out.println(book);
    }

    @BeforeEach
    void dependenciesShouldBeNotNull() {
        assertNotNull(bookRepo, "BookRepoJpa не должно быть null");
        assertNotNull(personService, "PersonService не должно быть null");
    }

    @Nested
    @DisplayName("Throw Exception")
    class ThrowException {
        @Test
        @Tag("notFoundBook")
        void getBookById_ThrowRuntimeException_IfBookNotExist() {
            assertThatThrownBy(() -> {
                bookService.getBookById(100000);
            }).isInstanceOf(RuntimeException.class);
        }
    }

    @ParameterizedTest
    @Tag("notFoundBook")
    @ValueSource(strings = {"Как стать Тиранозавром", "Готовим на луне", "1234$@"})
    void getBookLikeName_NotFound_IfBookNotExist(String name) {
        var expectedBook = bookService.getBookLikeName(name);

        assertThat(expectedBook).isEmpty();
        verify(bookRepo).findByNameContainingIgnoreCase(name);
    }

    @Test
    @Tag("foundBook")
    void getBookById_ReturnFindingBook_IfBookExist() {
        Book actualBook = bookService.getBookById(1);

        assertAll(
                () -> assertThat(actualBook).isNotNull(),
                () -> assertThat(actualBook.getName()).isNotNull(),
                () -> assertThat(actualBook.getAuthor()).isNotNull(),
                () -> assertThat(actualBook.getYearPublishing()).isNotNull()
        );
        verify(bookRepo).findById(1);
    }

    @ParameterizedTest(name = "({index})Get page with page number = {0}")
    @MethodSource("getArgumentsForPageBookTest")
    void getPageBook_ParameterizedTest(Integer pageNumber, boolean desc) {

        Page<Book> actualPage = bookService.getPageBook(pageNumber, desc);

        assertAll(
                () -> assertThat(actualPage).isNotEmpty(),
                () -> assertThat(actualPage.getTotalPages()).isEqualTo(1)
        );
    }

    static Stream<Arguments> getArgumentsForPageBookTest() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(0, true)
        );
    }
}