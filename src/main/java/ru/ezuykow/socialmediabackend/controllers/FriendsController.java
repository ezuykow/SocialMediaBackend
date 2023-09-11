package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    //-----------------API END-------------------
}
