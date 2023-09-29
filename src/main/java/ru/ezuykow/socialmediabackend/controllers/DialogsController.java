package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ezuykow
 */
@Hidden
@Tag(name = "Диалоги", description = "Заглушка")
@RestController
@RequestMapping("/dialogs")
@RequiredArgsConstructor
public class DialogsController {


    //-----------------API START-----------------

    @GetMapping()
    @Operation(summary = "Получить диалоги пользователя", description = "Заглушка - реализованно отдельным модулем")
    @SecurityRequirement(name = "JWT")
    @ApiResponse(responseCode = "200", description = "ОК", content = @Content())
    public ResponseEntity<?> getMyDialogs(Authentication auth) {
        return ResponseEntity.ok().build();
    }

    //-----------------API END-------------------
}
