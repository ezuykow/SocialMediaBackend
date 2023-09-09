package ru.ezuykow.socialmediabackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ezuykow.socialmediabackend.dto.FriendDTO;
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
public class FriendsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //-----------------API START-----------------

    public Map<String, List<FriendDTO>> getFriendsRequests(String initiatorUsername) {
        User initiator = userRepository.findUserByUsername(initiatorUsername)
                .orElseThrow(UserNotFoundException::new);

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
        User initiator = userRepository.findUserByUsername(fromUsername)
                .orElseThrow(() -> new UserNotFoundException(fromUsername));
        User target = userRepository.findUserByUsername(toUsername)
                .orElseThrow(() -> new UserNotFoundException(toUsername));

        initiator.getOutcomeFriendsRequests().add(target);
        initiator.getSubscribedTo().add(target);
        target.getIncomeFriendsRequests().add(initiator);

        userRepository.saveAll(List.of(initiator, target));
    }

    public void dropFriendRequest(String fromUsername, String toUsername) {
        User initiator = userRepository.findUserByUsername(fromUsername)
                .orElseThrow(() -> new UserNotFoundException(fromUsername));
        User target = userRepository.findUserByUsername(toUsername)
                .orElseThrow(() -> new UserNotFoundException(toUsername));

        initiator.getOutcomeFriendsRequests().remove(target);
        target.getIncomeFriendsRequests().remove(initiator);

        userRepository.saveAll(List.of(initiator, target));
    }

    public void rejectFriendRequest(String initiatorUsername, String fromUsername) {
        User initiator = userRepository.findUserByUsername(initiatorUsername)
                .orElseThrow(() -> new UserNotFoundException(initiatorUsername));
        User target = userRepository.findUserByUsername(fromUsername)
                .orElseThrow(() -> new UserNotFoundException((fromUsername)));

        initiator.getIncomeFriendsRequests().remove(target);
        target.getOutcomeFriendsRequests().remove(initiator);

        userRepository.saveAll(List.of(initiator, target));
    }
    //-----------------API END-------------------
}
