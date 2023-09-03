package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if (userOpt.isPresent()) {
            return new ru.ezuykow.socialmediabackend.security.UserDetails(userOpt.get());
        }

        throw new UsernameNotFoundException("Username is incorrect!");
    }
}
