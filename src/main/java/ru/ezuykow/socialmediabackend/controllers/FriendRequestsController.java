package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.services.FriendRequestsService;

import java.util.Map;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/friends/requests")
@RequiredArgsConstructor
public class FriendRequestsController {

    private final FriendRequestsService friendRequestsService;

    //-----------------API START-----------------

    @GetMapping()
    public ResponseEntity<?> getFriendsRequests(Authentication auth) {
        return ResponseEntity.ok(friendRequestsService.getFriendsRequests(auth.getName()));
    }

    @PostMapping("/outcome")
    public ResponseEntity<?> sendFriendsRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        if (auth.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error:", "Dont send friend request to self!"
            ));
        }

        friendRequestsService.sendFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/outcome")
    public ResponseEntity<?> dropFriendRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendRequestsService.dropFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/income")
    public ResponseEntity<?> rejectRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendRequestsService.rejectFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
