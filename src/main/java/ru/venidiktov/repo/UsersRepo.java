package ru.venidiktov.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.venidiktov.model.Users;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
}
