package ru.ezuykow.socialmediabackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.UserSubscriberDTO;
import ru.ezuykow.socialmediabackend.entities.Subscribe;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.exceptions.UserNotFoundException;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;
import ru.ezuykow.socialmediabackend.repositories.SubscribeRepository;
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
    private final SubscribeRepository subscribeRepository;

    //-----------------API START-----------------

    @Transactional
    public Map<String, List<UserSubscriberDTO>> findSubscribes(String username) {
        User targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        System.out.println(targetUser.getSubscribedTo());

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
            subscribeRepository.save(new Subscribe(0, initiator, target));
        } else {
            subscribeRepository.deleteBySubscriberAndSubscribedToUser(initiator, target);
        }
    }

    //-----------------API END-----------------
}
