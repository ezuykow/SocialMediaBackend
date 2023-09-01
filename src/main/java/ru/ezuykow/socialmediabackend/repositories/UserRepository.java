package ru.ezuykow.socialmediabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.socialmediabackend.entities.User;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmailOrUsername(String email, String username);
}
