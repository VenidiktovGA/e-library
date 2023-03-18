package ru.venidiktov.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.venidiktov.model.Users;
import ru.venidiktov.repo.UsersRepo;

@Service
public class RegistrationService {

    private final UsersRepo usersRepo;

    public RegistrationService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Transactional
    public void registration(Users user) {
        usersRepo.save(user);
    }

}
