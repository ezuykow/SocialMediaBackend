package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/dialogs")
@RequiredArgsConstructor
public class DialogsController {


    //-----------------API START-----------------

    public ResponseEntity<?> getMyDialogs(Authentication auth) {
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
