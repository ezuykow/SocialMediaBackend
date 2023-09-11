package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.exceptions.UserNotFoundException;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.util.Set;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserRepository userRepository;

    //-----------------API START-----------------

    public Set<User> getMyFriends(String initiatorUsername) {
        User initiator = userRepository.findUserByUsername(initiatorUsername)
                .orElseThrow(() -> new UserNotFoundException(initiatorUsername));

        return initiator.getFriends();
    }

    //-----------------API END-------------------
}
