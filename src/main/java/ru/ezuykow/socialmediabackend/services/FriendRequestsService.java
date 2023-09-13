package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.FriendDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;

import java.util.List;
import java.util.Map;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class FriendRequestsService {

    private final UserService userService;
    private final FriendsService friendsService;
    private final SubscribeService subscribeService;
    private final UserMapper userMapper;

    //-----------------API START-----------------

    public Map<String, List<FriendDTO>> getFriendsRequests(String initiatorUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);

        List<FriendDTO> fromMe = initiator.getOutcomeFriendsRequests()
                .stream().map(userMapper::mapUserToFriendDto).toList();
        List<FriendDTO> toMe = initiator.getIncomeFriendsRequests()
                .stream().map(userMapper::mapUserToFriendDto).toList();

        return Map.of(
                "to_me", toMe,
                "from_me", fromMe
        );
    }

    public void sendFriendRequest(String fromUsername, String toUsername) {
        User initiator = userService.findUserByUsername(fromUsername);
        User target = userService.findUserByUsername(toUsername);

        initiator.getOutcomeFriendsRequests().add(target);
        initiator.getSubscribedTo().add(target);
        target.getIncomeFriendsRequests().add(initiator);

        userService.saveAll(List.of(initiator, target));
    }

    public void dropFriendRequest(String fromUsername, String toUsername) {
        User initiator = userService.findUserByUsername(fromUsername);
        User target = userService.findUserByUsername(toUsername);

        removeRequest(initiator, target);

        userService.saveAll(List.of(initiator, target));
    }

    public void rejectFriendRequest(String initiatorUsername, String fromUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        User target = userService.findUserByUsername(fromUsername);

        initiator.getIncomeFriendsRequests().remove(target);
        target.getOutcomeFriendsRequests().remove(initiator);

        userService.saveAll(List.of(initiator, target));
    }

    public boolean acceptFriendsRequest(String initiatorUsername, String targetUsername) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        User target = userService.findUserByUsername(targetUsername);

        if (initiator.getIncomeFriendsRequests().contains(target)) {
            removeRequest(target, initiator);
            friendsService.addToFriends(initiator, target);
            subscribeService.addSubscribe(initiator, target);
            return true;
        }

        return false;
    }

    //-----------------API END-------------------

    private void removeRequest(User fromUser, User toUser) {
        fromUser.getOutcomeFriendsRequests().remove(toUser);
        toUser.getIncomeFriendsRequests().remove(fromUser);
    }
}
