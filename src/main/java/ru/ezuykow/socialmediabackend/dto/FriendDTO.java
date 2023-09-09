package ru.ezuykow.socialmediabackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class FriendDTO {

    private int userId;
    private String username;
}
