package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import ru.venidiktov.dao.BookDao;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;

import java.util.List;

@Service
public class BookService {

    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBook() {
        return bookDao.getAll();
    }

    public Book getBookById(int id) {
        return bookDao.getById(id);
    }

    public void createBook(Book book) {
        bookDao.create(book);
    }

    public void updateBookById(int id, Book book) {
        book.setId(id);
        bookDao.update(book);
    }

    public void deleteBookById(int id) {
        bookDao.deleteById(id);
    }

    public List<Book> getBooksForPerson(int id) {
        return bookDao.getBooksForPerson(id);
    }

    public Person getPersonForBook(int id) {
        return bookDao.getPersonForBook(id);
    }
}
