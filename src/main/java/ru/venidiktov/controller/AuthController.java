package ru.venidiktov.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.venidiktov.model.Users;
import ru.venidiktov.service.RegistrationService;
import ru.venidiktov.validator.UsersValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsersValidator usersValidator;

    private final RegistrationService registrationService;

    public AuthController(UsersValidator usersValidator, RegistrationService registrationService) {
        this.usersValidator = usersValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("users") Users users) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("users") @Valid Users user, BindingResult bindingResult) {
        usersValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) return "/auth/registration";
        registrationService.registration(user);
        return "redirect:/auth/login";
    }
}
