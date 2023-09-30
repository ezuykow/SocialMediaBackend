package ru.ezuykow.socialmediabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Пользователь")
public class UserDTO {

    @NotNull(message = "Email must not be empty!")
    @Email(message = "Email must be valid!")
    @Size(min = 5, max = 100, message = "Email length must be between 5 and 50 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Email пользователя", example = "Ivan@gmail.com")
    private String email;

    @NotNull(message = "Username must not be empty!")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Username пользователя", example = "username")
    private String username;

    @NotNull(message = "Password must not be empty!")
    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Пароль пользователя", example = "password")
    private String password;
}
