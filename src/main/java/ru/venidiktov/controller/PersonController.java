package ru.venidiktov.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.venidiktov.model.Person;
import ru.venidiktov.service.PersonService;
import ru.venidiktov.validator.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/persons")
@PropertySource("classpath:settings.properties")
public class PersonController {

    @Value("${contextPath}")
    private String contextPath;

    private final PersonService personService;

    private final PersonValidator personValidator;

    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getIndexPage(@RequestParam(required = false) Integer pageNumber, Model model) {
        personService.resolveNplusOneProblem();
        Page<Person> page = personService.getPagePerson(pageNumber);
        model.addAttribute("persons", page.toList());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("page", page.getNumber());
        return "person/persons";
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
        return "redirect:/" + contextPath + "/persons";
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
        return "redirect:/" + contextPath + "/persons/" + id;
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePersonById(id);
        return "redirect:/" + contextPath + "/persons";
    }

    @GetMapping("/{id}")
    public String getPersonPage(Model model, @PathVariable("id") int id) {
        Person person = personService.getPersonByIdWithBook(id);
        model.addAttribute("person", person);
        model.addAttribute("books", person.getBookList());
        return "person/person";
    }
}
