package ru.venidiktov.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.enums.Role;
import ru.venidiktov.model.Users;
import ru.venidiktov.repo.UsersRepo;

@Service
public class RegistrationService {

    private final UsersRepo usersRepo;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registration(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        usersRepo.save(user);
    }

}
