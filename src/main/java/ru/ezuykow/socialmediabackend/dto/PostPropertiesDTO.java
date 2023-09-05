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
public class PostPropertiesDTO {

    @NotNull(message = "Title must not be empty")
    @Size(min = 2, max  = 100, message = "Title must be between 2 and 100 characters!")
    private String title;

    @NotNull(message = "Post's text must not be empty")
    @Size(min = 2, max  = 4096, message = "Text must be between 2 and 4096 characters!")
    private String text;
}
