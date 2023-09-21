package ru.ezuykow.socialmediabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezuykow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDTO {

    private String jwtToken;

}
