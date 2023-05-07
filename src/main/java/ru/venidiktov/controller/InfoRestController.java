package ru.venidiktov.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.venidiktov.model.Book;
import ru.venidiktov.repo.BookRepoJpa;

@RestController
@RequestMapping("/info")
public class InfoRestController {

    private BookRepoJpa bookRepository;

    public InfoRestController(BookRepoJpa bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String sayInfo() {
        return "Info";
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
