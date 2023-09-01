package ru.ezuykow.socialmediabackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class UserDTO {

    @Email(message = "Email must be valid!")
    @Size(max = 100, message = "Email length must be less than 50 characters!")
    private String email;

    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;

    @NotNull
    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100 characters!")
    private String password;
}
