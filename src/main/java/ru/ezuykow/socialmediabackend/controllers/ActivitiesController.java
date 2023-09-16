package ru.ezuykow.socialmediabackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ezuykow.socialmediabackend.services.PostService;

import java.util.List;
import java.util.Map;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final PostService postService;

    //-----------------API START-----------------

    @GetMapping()
    public ResponseEntity<?> getActivities(
            Authentication auth,
            @RequestParam("page") int page,
            @RequestParam("count") int count
    ) {
        List<String> paramErrors = postService.validateRequestParams(page, count);

        if (!paramErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors:", paramErrors));
        }

        return ResponseEntity.ok(postService.getIdolsPostDTOs(auth.getName(), page, count));
    }

    //-----------------API END-------------------
}
