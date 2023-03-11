package ru.venidiktov.repo;

import org.junit.jupiter.api.Test;
import ru.venidiktov.BaseTest;
import ru.venidiktov.model.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookRepoTest extends BaseTest {

    @Test
    public void isExistsBook() {
        List<Book> books = bookRepo.findByNameContainingIgnoreCase("колец");
        Book lordOfTheRings = books.get(0);

        assertThat(books.size()).isEqualTo(1);
        assertThat(lordOfTheRings.getName()).isEqualToIgnoringCase("Властелин колец");
        assertThat(lordOfTheRings.getOwner().getName()).isEqualToIgnoringCase("Ivan");
    }
}
