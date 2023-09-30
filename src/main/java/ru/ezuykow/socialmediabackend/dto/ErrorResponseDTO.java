package ru.ezuykow.socialmediabackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Ошибки")
public class ErrorResponseDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Список ошибок", example = "Неправильные параметры запроса")
    private List<String> errors;

}
