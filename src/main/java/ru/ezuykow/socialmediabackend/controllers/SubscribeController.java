package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.ezuykow.socialmediabackend.services.SubscribeService;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/subscribes")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    @GetMapping()
    public ResponseEntity<?> getSubscribes(Authentication auth) {
        return ResponseEntity.status(HttpStatus.OK).body(subscribeService.findSubscribes(auth.getName()));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> addSubscribe(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        subscribeService.changeSubscribe(auth.getName(), true, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> removeSubscribe(
            Authentication auth,
            @RequestParam("target") String username
    ) {
        subscribeService.changeSubscribe(auth.getName(), false, username);
        return ResponseEntity.ok().build();
    }
}
