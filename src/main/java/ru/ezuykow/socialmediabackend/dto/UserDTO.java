package ru.ezuykow.socialmediabackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private String email;
    private String username;
    private String password;
}
