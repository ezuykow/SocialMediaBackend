package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //-----------------API START-----------------

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void checkExistence(User user, BindingResult bindingResult) {
        Optional<User> opt = userRepository.findUserByEmailOrUsername(user.getEmail(), user.getUsername());
        if (opt.isPresent()) {
            bindingResult.addError(new ObjectError("User", "This user already exists!"));
        }
    }

    //-----------------API END-----------------
}
