package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.services.FriendsService;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    //-----------------API START-----------------

    @GetMapping()
    public ResponseEntity<?> getMyFriends(Authentication auth) {
        return ResponseEntity.ok(friendsService.getMyFriends(auth.getName()));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFriend(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        friendsService.deleteFriend(auth.getName(), username);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
