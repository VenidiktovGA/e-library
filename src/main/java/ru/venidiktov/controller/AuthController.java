package ru.venidiktov.controller;

import jakarta.validation.Valid;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.venidiktov.dto.AuthenticationDto;
import ru.venidiktov.dto.UserDto;
import ru.venidiktov.model.Users;
import ru.venidiktov.security.JWTUtil;
import ru.venidiktov.service.RegistrationService;
import ru.venidiktov.validator.UsersValidator;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersValidator usersValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    public AuthController(UsersValidator usersValidator, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.usersValidator = usersValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute("users") Users users) {
//        return "auth/registration";
//    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        var user = convertToUsers(userDto);
        usersValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) return Map.of("message", "Ошибка!");
        registrationService.registration(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("jwt-token", token);
    }

    /**
     * Когда jwt токен просрочится этот метод выдаст зарегистрированному пользователю новый токен
     */
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDto authenticationDto) {
        //Класс UsernamePasswordAuthenticationToken это стандартный класс для инкапсуляции логина и пароля в spring
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDto.getUsername(),
                authenticationDto.getPassword()
        );

        //Метод который предоставляет SpringSecurity для проверки логина и пароля (так же как и на странице login по умолчанию)
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (AuthenticationException e) {
            return Map.of("message", "Incorrect username or password");
        }

        String token = jwtUtil.generateToken(authenticationDto.getUsername());
        return Map.of("jwt-token", token);
    }

    public Users convertToUsers(UserDto userDto) {
        return modelMapper.map(userDto, Users.class);
    }
}
