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

import javax.validation.Valid;

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
            model.addAttribute("owner", bookService.getBookOwner(book.getPersonId()));
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
    public String createBook(@ModelAttribute @Valid Book book, Errors errors) {
        if (errors.hasErrors()) {
            return "/book/new";
        }
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
            @ModelAttribute @Valid Book book,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            return "book/edit";
        }
        bookService.updateBookById(id, book);
        return "redirect:/" + contextPath + "/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String addPersonForBook(
            @PathVariable("id") int id,
            @ModelAttribute Book book
    ) {
        bookService.assignBook(id, book);
        return "redirect:/" + contextPath + "/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.releaseBook(id);
        return "redirect:/" + contextPath + "/books/" + id;
    }

}
