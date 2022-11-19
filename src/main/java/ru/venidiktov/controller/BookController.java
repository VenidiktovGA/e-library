package ru.venidiktov.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.venidiktov.model.Book;
import ru.venidiktov.service.BookService;
import ru.venidiktov.service.PersonService;

@Controller
@RequestMapping("/books")
@PropertySource("classpath:settings.properties")
public class BookController {

    @Value("${contextPath}")
    private String contextPath;

    private final BookService bookService;

    private final PersonService personService;

    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String getIndexPage(Model model) {
        model.addAttribute("books", bookService.getAllBook());
        return "book/books";
    }

    @GetMapping("/{id}")
    public String getBookPage(@PathVariable("id") int id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        if (book.getPersonId() != null) {
            model.addAttribute("person", bookService.getPersonForBook(book.getPersonId()));
        } else {
            model.addAttribute("persons", personService.getAllPerson());
        }
        return "book/book";
    }

    @GetMapping("/new")
    public String getPageCreateBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @GetMapping("{id}/edit")
    public String getPageUpdateBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book/edit";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute Book book, Errors errors) {
        bookService.createBook(book);
        return "redirect:/" + contextPath + "/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBookById(id);
        return "redirect:/" + contextPath + "/books";
    }

    @PatchMapping("/{id}")
    public String updateBook(
            @PathVariable("id") int id,
            @ModelAttribute Book book,
            Errors errors
    ) {
        bookService.updateBookById(id, book);
        return "redirect:/" + contextPath + "/books/" + id;
    }

}
