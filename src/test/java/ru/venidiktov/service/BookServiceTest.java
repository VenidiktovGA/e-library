package ru.venidiktov.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import org.springframework.data.domain.Page;
import ru.venidiktov.BaseTest;
import ru.venidiktov.extension.ConditionalExtension;
import ru.venidiktov.extension.GlobalExtension;
import ru.venidiktov.extension.PostProcessingExtension;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.paramresolver.BookServiceParamResolver;

@ExtendWith({
        BookServiceParamResolver.class,
        GlobalExtension.class,
        PostProcessingExtension.class,
        ConditionalExtension.class,
//        ThrowableExtension.class
})
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
    void getBookLikeName_NotFound_IfBookNotExist(String name) throws RuntimeException {
        if (true)
            throw new RuntimeException(); //Смотрел как работает самописный перехват исключение в тесте ThrowableExtension
        var expectedBook = bookService.getBookLikeName(name);

        assertThat(expectedBook).isEmpty();
        verify(bookRepo).findByNameContainingIgnoreCase(name);
    }

    @ParameterizedTest
    @Tag("foundBook")
    @CsvFileSource(resources = "/book-data-test.csv", delimiter = ',', numLinesToSkip = 1)
    void getBookById_ReturnFindingBook_IfBookExist(Integer id, String bookName, String author, String year_publishing) {
        Book actualBook = bookService.getBookById(id);

        assertAll(
                () -> assertThat(actualBook).isNotNull(),
                () -> assertThat(actualBook.getName()).isEqualToIgnoringCase(bookName),
                () -> assertThat(actualBook.getAuthor()).isEqualToIgnoringCase(author),
                () -> assertThat(actualBook.getYearPublishing()).isEqualTo(year_publishing)
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

    @Test
    void assignBook_success() {
        Person person = new Person();
        person.setName("Ivan");
        person.setSurname("Ivanov");
        person.setBirthday(LocalDate.now());
        personRepo.saveAndFlush(person);
        Book book = new Book();
        book.setName("Java 21");
        book.setAuthor("Pupkin");
        book.setYearPublishing(LocalDate.now());
        bookRepo.saveAndFlush(book);

        bookService.assignBook(book.getId(), person);
        Book existBook = bookRepo.getById(book.getId());
        System.out.println("dsdf" + 10 + 10);

        var argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(personService, times(1)).getPersonById(argumentCaptor.capture());
        assertAll(
                () -> assertThat(existBook).isNotNull(),
                () -> assertThat(existBook.getOwner()).isNotNull(),
                () -> assertThat(argumentCaptor.getValue()).isEqualTo(person.getId())
        );
    }

    static Stream<Arguments> getArgumentsForPageBookTest() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(0, true)
        );
    }

    @Nested
    @DisplayName("SLA time response")
    class SLATimeResponse {

        @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
        @Tag("SLA")
        void getBookLikeName_NotFound_IfBookNotExist(RepetitionInfo repetitionInfo) {
            assertTimeout(Duration.ofSeconds(1L), () -> bookService.getBookLikeName("олец"));
            verify(bookRepo, times(repetitionInfo.getCurrentRepetition())).findByNameContainingIgnoreCase("олец");
        }
    }
}