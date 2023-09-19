package ru.ezuykow.socialmediabackend.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ezuykow
 */
@Data
public class ErrorResponseDTO {

    private List<String> errors;

    public ErrorResponseDTO (List<String> errors) {
        this.errors = errors;
    }
}
