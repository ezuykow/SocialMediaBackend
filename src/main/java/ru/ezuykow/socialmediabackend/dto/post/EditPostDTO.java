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
@Schema(name = "Редактированный пост", description = "Редактированные заголовок и текст поста")
public class EditPostDTO {

    @NotNull(message = "Post id must not ne null!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID редактируемого поста", example = "123")
    private long postId;

    @Size(max  = 100, message = "Title must be less than 100 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Заголовок поста", example = "Заголовок")
    private String title;

    @Size(max  = 4096, message = "Text must be less than 4096 characters!")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Текст поста", example = "Очень интересная история")
    private String text;
}
