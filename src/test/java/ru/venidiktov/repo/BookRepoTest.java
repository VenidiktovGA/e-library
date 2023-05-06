package ru.venidiktov.repo;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.venidiktov.BaseTest;
import ru.venidiktov.model.Book;

public class BookRepoTest extends BaseTest {

    @Test
    public void findByNameContainingIgnoreCase_success_ifBookExist() {
        List<Book> books = bookRepo.findByNameContainingIgnoreCase("колец");
        Book lordOfTheRings = books.get(0);

        assertThat(books.size()).isEqualTo(1);
        assertThat(lordOfTheRings.getName()).isEqualToIgnoringCase("Властелин колец");
        assertThat(lordOfTheRings.getOwner().getName()).isEqualToIgnoringCase("Ivan");
    }
}
