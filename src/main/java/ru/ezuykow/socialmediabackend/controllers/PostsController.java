package ru.ezuykow.socialmediabackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ezuykow.socialmediabackend.dto.post.EditPostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.services.PostService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            Authentication auth,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("properties") @Valid PostPropertiesDTO postPropertiesDTO,
            BindingResult bindingResult
    ) {
        ResponseEntity<?> errorResponse = checkBindingResults(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        PostDTO postDto = postService.createPost(auth.getName(), image, postPropertiesDTO);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping()
    public ResponseEntity<?> getPosts(
            @RequestParam("page") int page,
            @RequestParam("count") int count
    ) {
        List<String> paramErrors = postService.validateRequestParams(page, count);

        if (!paramErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors:", paramErrors));
        }

        return ResponseEntity.ok(postService.getPostDTOs(page, count));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> patchPost(
            Authentication auth,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("properties") @Valid EditPostDTO editPostDTO,
            BindingResult bindingResult
    ) {
        Optional<Post> targetPostOpt = postService.findById(editPostDTO.getPostId());
        if (targetPostOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post targetPost = targetPostOpt.get();
        if (!targetPost.getAuthor().getUsername().equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ResponseEntity<?> errorResponse = checkBindingResults(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        PostDTO postDto = postService.patchPost(targetPost, image, editPostDTO);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePost(Authentication auth, @RequestParam("id") long postId) {
        Optional<Post> targetPostOpt = postService.findById(postId);
        if (targetPostOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post targetPost = targetPostOpt.get();
        if (!targetPost.getAuthor().getUsername().equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    //-----------------API END-----------------

    private ResponseEntity<?> checkBindingResults(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors: ",
                    bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList())
            ));
        }
        return null;
    }
}