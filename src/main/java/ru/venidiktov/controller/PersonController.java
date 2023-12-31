package ru.venidiktov.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.venidiktov.model.Person;
import ru.venidiktov.model.Users;
import ru.venidiktov.security.UsersDetails;
import ru.venidiktov.service.PersonService;
import ru.venidiktov.validator.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    private final PersonValidator personValidator;

    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getIndexPage(
            @RequestParam(required = false) Integer pageNumber,
            Model model,
            HttpSession session,
            HttpServletResponse response,
            @CookieValue(value = "eLibraryStatus", required = false) String eLibraryStatus
    ) {
        String attribute = "countIn";
        Integer countIn = Optional.ofNullable((Integer) session.getAttribute(attribute)).orElse(0) + 1;
        String sessionId = session.getId();
        session.setAttribute(attribute, countIn);
        model.addAttribute(attribute, countIn);
        model.addAttribute("sessionId", sessionId);

        Cookie cookie = new Cookie("eLibraryStatus", eLibraryStatus == null ? "firstPersonsPageVisited" : "notFirstPersonsPageVisited");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);


        personService.resolveNplusOneProblem();
        Page<Person> page = personService.getPagePerson(pageNumber);
        model.addAttribute("persons", page.toList());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("page", page.getNumber());
        return "person/persons";
    }

    @GetMapping("/user-info")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails userDetails = (UsersDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();
        System.out.println(user);
        return "index";
    }

    @GetMapping("/new")
    public String getNewPage(@ModelAttribute("person") Person person) {
        return "person/new";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.getPersonById(id));
        return "person/edit";
    }

    @PostMapping("/new")
    public String createPerson(@ModelAttribute @Valid Person person, Errors errors) {
        personValidator.validate(person, errors);
        if (errors.hasErrors()) {
            return "person/new";
        }
        personService.createPerson(person);
        return "redirect:/persons";
    }

    @PatchMapping("/{id}")
    public String updatePerson(
            @ModelAttribute("person") @Valid Person person,
            Errors errors,
            @PathVariable("id") int id
    ) {
        personValidator.validate(person, errors);
        if (errors.hasErrors()) {
            return "person/edit";
        }
        personService.updatePersonById(id, person);
        return "redirect:/persons/" + id;
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePersonById(id);
        return "redirect:/persons";
    }

    @GetMapping("/{id}")
    public String getPersonPage(Model model, @PathVariable("id") int id) {
        Person person = personService.getPersonByIdWithBook(id);
        model.addAttribute("person", person);
        model.addAttribute("books", person.getBookList());
        return "person/person";
    }
}
