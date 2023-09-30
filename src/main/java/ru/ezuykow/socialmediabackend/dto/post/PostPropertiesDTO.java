package ru.ezuykow.socialmediabackend.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@Schema(name = "Параметры поста", description = "Заголовок и текст поста")
public class PostPropertiesDTO {

    @NotNull(message = "Title must not be empty")
    @Size(min = 2, max  = 100, message = "Title must be between 2 and 100 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Заголовок поста", example = "Заголовок")
    private String title;

    @NotNull(message = "Post's text must not be empty")
    @Size(min = 2, max  = 4096, message = "Text must be between 2 and 4096 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Текст поста", example = "Текст")
    private String text;
}
