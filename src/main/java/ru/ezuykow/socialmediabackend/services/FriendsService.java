package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.entities.User;

import java.util.Set;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserService userService;

    //-----------------API START-----------------

    public Set<User> getMyFriends(String initiatorUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        return initiator.getFriends();
    }

    public void addToFriends(User fUser, User sUser) {
        fUser.getFriends().add(sUser);
        sUser.getFriends().add(fUser);
    }

    //-----------------API END-------------------
}
