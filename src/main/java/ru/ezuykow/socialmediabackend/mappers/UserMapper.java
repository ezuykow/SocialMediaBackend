package ru.ezuykow.socialmediabackend.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.ezuykow.socialmediabackend.dto.FriendDTO;
import ru.ezuykow.socialmediabackend.dto.UserSubscriberDTO;
import ru.ezuykow.socialmediabackend.dto.UserDTO;
import ru.ezuykow.socialmediabackend.entities.User;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    //-----------------API START-----------------

    public User mapUserDtoToUser(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserSubscriberDTO mapUserToSubscriberDto(User user) {
        return modelMapper.map(user, UserSubscriberDTO.class);
    }

    public FriendDTO mapUserToFriendDto(User user) {
        return modelMapper.map(user, FriendDTO.class);
    }

    //-----------------API END-----------------
}
