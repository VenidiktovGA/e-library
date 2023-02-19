package ru.venidiktov.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.venidiktov.repo.UsersRepo;
import ru.venidiktov.security.UsersDetails;

@Service
public class UsersDetailsService implements UserDetailsService {

    private UsersRepo usersRepo;

    public UsersDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UsersDetails(usersRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Пользователь с именем " + username + " не был найден!")
                )
        );
    }
}