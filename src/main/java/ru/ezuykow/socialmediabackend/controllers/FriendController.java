package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.services.FriendsService;

import java.util.Map;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendsService friendsService;

    //-----------------API START-----------------

    @GetMapping("/requests")
    public ResponseEntity<?> getFriendsRequests(Authentication auth) {
        return ResponseEntity.ok(friendsService.getFriendsRequests(auth.getName()));
    }

    @PostMapping("/requests/outcome")
    public ResponseEntity<?> sendFriendsRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        if (auth.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error:", "Dont send friend request to self!"
            ));
        }

        friendsService.sendFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/requests/outcome")
    public ResponseEntity<?> dropFriendRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendsService.dropFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/requests/income")
    public ResponseEntity<?> rejectRequest(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendsService.rejectFriendRequest(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
