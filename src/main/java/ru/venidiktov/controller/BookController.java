package ru.venidiktov.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;
import ru.venidiktov.service.BookService;
import ru.venidiktov.service.PersonService;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final PersonService personService;

    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping
    public String getIndexPage(@RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) boolean decs,
                               Model model) {
        Page<Book> page = bookService.getPageBook(pageNumber, decs);
        model.addAttribute("books", page.toList());
        model.addAttribute("decs", decs);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("page", page.getNumber());
        return "book/books";
    }

    @GetMapping("/{id}")
    public String getBookPage(
            @PathVariable("id") int id,
            @ModelAttribute("person") Person person,
            Model model
    ) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        if (book.getOwner() != null) {
            model.addAttribute("owner", bookService.getBookOwner(book.getOwner().getId()));
        } else {
            model.addAttribute("persons", personService.getAllPerson());
        }
        return "book/book";
    }

    @GetMapping("/new")
    public String getPageCreateBook(@ModelAttribute("book") Book book) {
        return "book/new";
    }

    @GetMapping("/search")
    public String getPageCreateBook(@RequestParam(required = false) String bookName, Model model) {
        model.addAttribute("books", bookService.getBookLikeName(bookName));
        return "book/search";
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
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
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
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(
            @PathVariable("id") int bookId,
            @ModelAttribute Person person
    ) {
        bookService.assignBook(bookId, person);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.releaseBook(id);
        return "redirect:/books/" + id;
    }

}
