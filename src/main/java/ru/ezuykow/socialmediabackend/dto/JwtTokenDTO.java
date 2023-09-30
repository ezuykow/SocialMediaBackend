package ru.ezuykow.socialmediabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Токен аутентификации")
public class JwtTokenDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Токен")
    private String jwtToken;

}
