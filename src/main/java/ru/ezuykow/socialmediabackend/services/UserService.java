package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.exceptions.UserNotFoundException;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //-----------------API START-----------------

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    //-----------------API END-------------------
}
