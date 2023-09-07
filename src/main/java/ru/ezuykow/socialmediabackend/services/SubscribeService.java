package ru.ezuykow.socialmediabackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.UserSubscriberDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.exceptions.UserNotFoundException;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.util.List;
import java.util.Map;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //-----------------API START-----------------

    public Map<String, List<UserSubscriberDTO>> findSubscribes(String username) {
        User targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);

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
        User target = userRepository.findUserByUsername(targetUsername)
                .orElseThrow(UserNotFoundException::new);
        User initiator = userRepository.findUserByUsername(initiatorUsername)
                .orElseThrow(UserNotFoundException::new);

        if (subscribe) {
            initiator.getSubscribedTo().add(target);
        } else {
            initiator.getSubscribedTo().remove(target);
        }
    }

    //-----------------API END-----------------
}
