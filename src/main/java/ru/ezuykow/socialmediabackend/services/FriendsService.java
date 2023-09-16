package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.FriendDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserService userService;
    private final SubscribeService subscribeService;
    private final UserMapper userMapper;

    //-----------------API START-----------------

    public List<FriendDTO> getMyFriends(String initiatorUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        return initiator.getFriends().stream().map(userMapper::mapUserToFriendDto).toList();
    }

    public void deleteFriend(String initiatorUsername, String targetUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        User target = userService.findUserByUsername(targetUsername);

        removeFromFriends(initiator, target);
        subscribeService.removeSubscribe(initiator, target);

        userService.saveAll(List.of(initiator, target));
    }

    public void addToFriends(User fUser, User sUser) {
        fUser.getFriends().add(sUser);
        sUser.getFriends().add(fUser);
    }

    public void removeFromFriends(User fUser, User sUser) {
        fUser.getFriends().remove(sUser);
        sUser.getFriends().remove(fUser);
    }

    //-----------------API END-------------------
}
