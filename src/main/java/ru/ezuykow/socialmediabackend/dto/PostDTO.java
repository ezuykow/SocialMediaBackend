package ru.ezuykow.socialmediabackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class PostDTO {

    private Integer postId;
    private Integer authorId;
    private String title;
    private String text;
    private String image;
    private LocalDateTime createdAt;

}
