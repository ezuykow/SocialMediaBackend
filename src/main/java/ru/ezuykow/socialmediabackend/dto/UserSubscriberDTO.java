package ru.ezuykow.socialmediabackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
public class UserSubscriberDTO {

    private int userId;
    private String username;
}
