package ru.ezuykow.socialmediabackend.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@Schema(name = "Пост")
public class PostDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID поста", example = "123")
    private Integer postId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID пользователя, создавшего пост", example = "99")
    private Integer authorId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Username пользователя, создавшего пост", example = "Ivan")
    private String authorUsername;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Заголовок поста", example = "Заголовок")
    private String title;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Текст поста", example = "Очень интересная история")
    private String text;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Название файла изображения к посту в файловой " +
            "системе, либо null при его отсутствии", example = "5.im")
    private String image;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Время создания поста UTC+0", example = "2023-09-30T12:25:57.8657511")
    private LocalDateTime createdAt;

}
