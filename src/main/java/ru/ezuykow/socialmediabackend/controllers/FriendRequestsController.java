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
import ru.ezuykow.socialmediabackend.services.FriendRequestsService;

import java.util.Collections;

/**
 * @author ezuykow
 */
@Tag(name = "Заявки в друзья")
@RestController
@RequestMapping("/friends/requests")
@RequiredArgsConstructor
public class FriendRequestsController {

    private final FriendRequestsService friendRequestsService;

    //-----------------API START-----------------

    @GetMapping()
    @Operation(summary = "Получить заявки в друзья пользователя")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Возвращает заявки в друзья",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/FriendsRequests"))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    public ResponseEntity<?> getFriendsRequests(Authentication auth) {
        return ResponseEntity.ok(friendRequestsService.getFriendsRequests(auth.getName()));
    }

    @PostMapping("/outcome")
    @Operation(summary = "Отправить заявку в друзья")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка в друзья отправлена",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Заявка в друзья самому себе",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, которому отправить заявку в друзья")
    public ResponseEntity<?> sendFriendsRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        if (auth.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponseDTO(Collections.singletonList("Dont send friend request to self!"))
            );
        }

        friendRequestsService.sendFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/income")
    @Operation(summary = "Принять заявку в друзья")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка в друзья принята",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Заявки от пользователя с таким username нет",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, от которого принять заявку в друзья")
    public ResponseEntity<?> acceptFriendsRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        boolean success = friendRequestsService.acceptFriendsRequest(auth.getName(), username);

        if (success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDTO(Collections.singletonList("Request from " + username + "not existent")
        ));
    }

    @DeleteMapping("/outcome")
    @Operation(summary = "Отменить заявку в друзья")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка в друзья отменена",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, заявку в друзья на которого нужно отменить")
    public ResponseEntity<?> dropFriendRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendRequestsService.dropFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/income")
    @Operation(summary = "Отклонить заявку в друзья")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка в друзья отклонена",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
    @Parameter(name = "target", description = "Username пользователя, от которого отклонить заявку в друзья")
    public ResponseEntity<?> rejectRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendRequestsService.rejectFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
