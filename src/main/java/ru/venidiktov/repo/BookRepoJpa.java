package ru.venidiktov.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.venidiktov.model.Book;

import java.util.List;

@Repository
public interface BookRepoJpa extends JpaRepository<Book, Integer> {

    @Modifying
    @Query("update Book b set b.owner = NULL where b.id = ?1")
    int release(int bookId);

    List<Book> findByNameContainingIgnoreCase(String name);
}
