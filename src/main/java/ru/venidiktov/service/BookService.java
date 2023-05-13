package ru.venidiktov.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.enums.SortType;
import ru.venidiktov.exception.BookNotFoundException;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.repo.BookRepoJpa;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepoJpa bookRepo;
    private final PersonService personService;

    public BookService(BookRepoJpa bookRepo, PersonService personService) {
        this.bookRepo = bookRepo;
        this.personService = personService;
    }

    public List<Book> getBookLikeName(String name) {
        if (name == null || name.length() < 1) {
            return null;
        } else {
            return bookRepo.findByNameContainingIgnoreCase(name);
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
                .orElseThrow(() -> new BookNotFoundException("Книги с таким id=" + id + " не существует!"));
    }

    @Transactional
    public void createBook(Book book) {
        bookRepo.save(book);
    }

    @Transactional
    public void updateBookById(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Книги с таким id=" + id + " не существует!"));
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        bookRepo.save(updatedBook);
    }

    @Transactional
    public void deleteBookById(int id) {
        bookRepo.deleteById(id);
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
