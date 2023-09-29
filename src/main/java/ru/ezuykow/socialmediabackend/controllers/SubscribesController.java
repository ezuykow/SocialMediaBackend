package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.dto.ErrorResponseDTO;
import ru.ezuykow.socialmediabackend.services.SubscribeService;

import java.util.Collections;

/**
 * @author ezuykow
 */
@Tag(name = "Подписки", description = "Управление подписками")
@RestController
@RequestMapping("/subscribes")
@RequiredArgsConstructor
public class SubscribesController {

    private final SubscribeService subscribeService;

    @GetMapping()
    @Operation(summary = "Получить исходящие и входящие подписки пользователя")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Возвращает подписки",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Subscribes"))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    public ResponseEntity<?> getSubscribes(Authentication auth) {
        return ResponseEntity.status(HttpStatus.OK).body(subscribeService.findSubscribes(auth.getName()));
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Подписаться")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Подписан на пользователя",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Подписка самого на себя",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, на которого подписаться")
    public ResponseEntity<?> addSubscribe(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        if (auth.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponseDTO(Collections.singletonList("Dont subscribe to self!")));
        }

        subscribeService.changeSubscribe(auth.getName(), true, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    @Operation(summary = "Отписаться")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отписан от пользователя",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, от которого отписаться")
    public ResponseEntity<?> removeSubscribe(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        subscribeService.changeSubscribe(auth.getName(), false, username);
        return ResponseEntity.ok().build();
    }
}
