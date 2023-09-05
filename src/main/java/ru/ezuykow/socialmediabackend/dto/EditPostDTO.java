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
public class EditPostDTO {

    @NotNull(message = "Post id must not ne null!")
    private long postId;

    @Size(max  = 100, message = "Title must be less than 100 characters!")
    private String title;

    @Size(max  = 4096, message = "Text must be less than 4096 characters!")
    private String text;
}
