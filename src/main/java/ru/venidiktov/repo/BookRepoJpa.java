package ru.venidiktov.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.venidiktov.model.Book;

import java.util.List;

@Repository
public interface BookRepoJpa extends JpaRepository<Book, Integer> {

    List<Book> findByPersonId(int id);

    @Modifying
    @Query("update Book b set b.personId = ?1 where b.id = ?2")
    int assign(int personId, int bookId);

    @Modifying
    @Query("update Book b set b.personId = NULL where b.id = ?1")
    int release(int personId);
}
