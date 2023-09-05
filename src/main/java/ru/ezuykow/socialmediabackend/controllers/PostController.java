package ru.ezuykow.socialmediabackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ezuykow.socialmediabackend.dto.PostDTO;
import ru.ezuykow.socialmediabackend.dto.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.services.PostService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            Authentication auth,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("properties") @Valid PostPropertiesDTO postPropertiesDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors: ",
                    bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList())
            ));
        }

        PostDTO postDto = postService.createPost(auth.getName(), image, postPropertiesDTO);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping()
    public ResponseEntity<?> getPosts(
            @RequestParam("page") int page,
            @RequestParam("count") int count
    ) {
        List<String> paramErrors = validateParams(page, count);

        if (!paramErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors:", paramErrors));
        }

        Page<Post> posts = postService.getPosts(page, count);
        return ResponseEntity.ok(posts.getContent());
    }

    private List<String> validateParams(int page, int count) {
        List<String> errors = new LinkedList<>();
        if (page < 0) {
            errors.add("Page number must be 0 or more!");
        }
        if (count < 1) {
            errors.add("Posts count on page must be 1 or more!");
        }
        return errors;
    }
}