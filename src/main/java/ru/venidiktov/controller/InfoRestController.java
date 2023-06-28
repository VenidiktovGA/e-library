package ru.venidiktov.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.venidiktov.model.Book;
import ru.venidiktov.security.UsersDetails;
import ru.venidiktov.service.BookService;

@RestController
@RequestMapping("/info")
public class InfoRestController {

    private final BookService bookService;

    public InfoRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String sayInfo() {
        return "Info";
    }

    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/show-user-info")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();

        return usersDetails.getUsername();
    }

}
