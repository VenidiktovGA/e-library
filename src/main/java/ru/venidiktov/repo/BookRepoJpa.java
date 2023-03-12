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

    // TODO: 3/12/2023 Тут возникает проблема 1000+1 для каждой книги будет делаться селект в таблицу Person
    //чтобы этого избежать нужно использовать ... нужно решить проблему с регистром @Query("select b from Book b LEFT JOIN FETCH b.owner where upper(b.name) like %?1%")
    List<Book> findByNameContainingIgnoreCase(String name);
}
