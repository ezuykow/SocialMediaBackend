package ru.ezuykow.socialmediabackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class AuthDTO {

    @NotNull
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters!")
    private String username;

    @NotNull
    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100 characters!")
    private String password;
}