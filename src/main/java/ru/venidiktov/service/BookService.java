package ru.venidiktov.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.dao.BookDao;
import ru.venidiktov.enums.SortType;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.BookRepoJpa;

import java.time.LocalDate;
import java.util.Arrays;
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

    public List<Book> getBookLikeName(String name) {
        if (name == null || name.length() < 1) {
            return null;
        } else {
            return bookRepo.findBookLikeName(name.toUpperCase());
        }
    }

    public Page<Book> getPageBook(Integer pageNumber, boolean desc) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        SortType sortType = Arrays.stream(SortType.values()).filter(e -> e.getDesc() == desc).findAny().get();
        return bookRepo.findAll(PageRequest.of(pageNumber, 10, Sort.by(sortType.getDirectionSort(), "yearPublishing")));
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
        book.setAssignDate(LocalDate.now());
    }

    @Transactional
    public void releaseBook(int id) {
        bookRepo.release(id);
    }
}
