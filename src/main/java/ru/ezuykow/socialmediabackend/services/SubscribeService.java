package ru.ezuykow.socialmediabackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.UserSubscriberDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;

import java.util.List;
import java.util.Map;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final UserService userService;
    private final UserMapper userMapper;

    //-----------------API START-----------------

    public Map<String, List<UserSubscriberDTO>> findSubscribes(String username) {
        User targetUser = userService.findUserByUsername(username);

        List<UserSubscriberDTO> toMe = targetUser.getSubscribers().stream()
                .map(userMapper::mapUserToSubscriberDto).toList();
        List<UserSubscriberDTO> fromMe = targetUser.getSubscribedTo().stream()
                .map(userMapper::mapUserToSubscriberDto).toList();

        return Map.of(
                "to_me", toMe,
                "from_me", fromMe
        );
    }

    @Transactional
    public void changeSubscribe(String initiatorUsername, boolean subscribe, String targetUsername) {
        User target = userService.findUserByUsername(targetUsername);
        User initiator = userService.findUserByUsername(initiatorUsername);

        if (subscribe) {
            addSubscribe(initiator, target);
        } else {
            removeSubscribe(initiator, target);
        }

        userService.save(initiator);
    }

    public void addSubscribe(User subscriber, User subscribeTo) {
        subscriber.getSubscribedTo().add(subscribeTo);
    }

    public void removeSubscribe(User subscriber, User subscribedTo) {
        subscriber.getSubscribedTo().remove(subscribedTo);
    }

    //-----------------API END-----------------
}
