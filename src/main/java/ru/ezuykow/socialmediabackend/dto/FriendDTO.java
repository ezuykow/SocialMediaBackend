package ru.ezuykow.socialmediabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@Schema(title = "Друг")
public class FriendDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID пользователя", example = "123")
    private int userId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Username пользователя", example = "ivan")
    private String username;
}
