package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ezuykow.socialmediabackend.dto.ErrorResponseDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostDTO;
import ru.ezuykow.socialmediabackend.services.PostService;

import java.util.List;

/**
 * @author ezuykow
 */
@Tag(name = "Лента активностей", description = "Действия с лентой активности")
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final PostService postService;

    //-----------------API START-----------------

    @Operation(summary = "Получить посты кумиров", description = "Пользователь должен быть аутентифицирован")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посты найдены",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameters({
        @Parameter(name = "page", description = "Номер отображаемой страницы, по-умолчанию 0"),
        @Parameter(name = "count", description = "Количество постов на странице, по-умолчанию 5")
    })
    @GetMapping()
    public ResponseEntity<?> getActivities(
            Authentication auth,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "count", defaultValue = "5", required = false) int count
    ) {
        List<String> paramErrors = postService.validateRequestParams(page, count);

        if (!paramErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(paramErrors));
        }

        return ResponseEntity.ok(postService.getIdolsPostDTOs(auth.getName(), page, count));
    }

    //-----------------API END-------------------
}
