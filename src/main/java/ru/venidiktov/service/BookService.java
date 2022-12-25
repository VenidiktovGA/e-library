package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.dao.BookDao;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.BookRepoJpa;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookDao bookDao;

    private final BookRepoJpa bookRepo;
    private final PersonService personService;

    public BookService(BookDao bookDao, BookRepoJpa bookRepo, PersonService personService) {
        this.bookDao = bookDao;
        this.bookRepo = bookRepo;
        this.personService = personService;
    }

    public List<Book> getAllBook() {
        return bookRepo.findAll();
    }

    public Book getBookById(int id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Книги с таким id=" + id + " не существует!"));
    }

    @Transactional
    public void createBook(Book book) {
        bookRepo.save(book);
    }

    @Transactional
    public void updateBookById(int id, Book book) {
        book.setId(id);
        bookRepo.save(book);
    }

    @Transactional
    public void deleteBookById(int id) {
        bookRepo.deleteById(id);
    }

    public List<Book> getBooksForPerson(int id) {
        return personService.getPersonById(id).getBookList();
    }

    public Person getBookOwner(int id) {
        return personService.getPersonById(id);
    }


    @Transactional
    public void assignBook(int bookId, Person person) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Книга с таким Id=" + bookId + " не найдена"));
        Person newOwner = personService.getPersonById(person.getId());
        book.setOwner(newOwner);
    }

    @Transactional
    public void releaseBook(int id) {
        bookRepo.release(id);
    }
}
