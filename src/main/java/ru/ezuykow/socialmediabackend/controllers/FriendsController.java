package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.dto.FriendDTO;
import ru.ezuykow.socialmediabackend.services.FriendsService;

/**
 * @author ezuykow
 */
@Tag(name = "Друзья")
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    //-----------------API START-----------------

    @GetMapping()
    @Operation(summary = "Получить друзей")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Возвращает друзей пользователя",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FriendDTO.class)))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    public ResponseEntity<?> getMyFriends(Authentication auth) {
        return ResponseEntity.ok(friendsService.getMyFriends(auth.getName()));
    }

    @DeleteMapping()
    @Operation(summary = "Удалить пользователя из друзей")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удаляет друга пользователя",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username удаляемого друга")
    public ResponseEntity<?> deleteFriend(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendsService.deleteFriend(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
