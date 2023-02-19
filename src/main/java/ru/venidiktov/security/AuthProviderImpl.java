package ru.venidiktov.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final UserDetailsService usersDetailsService;

    public AuthProviderImpl(UserDetailsService usersDetailsService) {
        this.usersDetailsService = usersDetailsService;
    }

    @Override
    //Данный метод вызывается самим Spring Security
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = usersDetailsService.loadUserByUsername(username);
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Неправильный пароль!");
        }
        //Данный класс реализует Authentication
        //Возвращаем Authentication с данными о человеке и его паролем
        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    //Данный метод определяет для какого объекта Authentication
    // работает данная реализация AuthProvider
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
