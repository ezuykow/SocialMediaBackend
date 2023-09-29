package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.ezuykow.socialmediabackend.dto.ErrorResponseDTO;
import ru.ezuykow.socialmediabackend.dto.post.EditPostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.services.PostService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Tag(name = "Посты", description = "Управление постами")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создать пост")
    @SecurityRequirement(name = "JWT")
    @RequestBody(description = "Заголовок, текст и изображение поста",
        content = @Content(mediaType = "multipart/form-data",
            schema = @Schema(
                    requiredProperties = {"Properties"},
                    type = "object",
                    properties = {
                            @StringToClassMapItem(key = "Image", value = MultipartFile.class),
                            @StringToClassMapItem(key = "Properties", value = PostPropertiesDTO.class)
                    }
            ))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пост создан",
                content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры",
                content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Запрос от не аутентифицированного пользователя",
                    content = @Content())
    })
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
    @Operation(summary = "Получить посты", description = "Возвращает посты по страницам от всех пользователей")
    @Parameters({
            @Parameter(name = "page", description = "Номер страницы начиная с 0"),
            @Parameter(name = "count", description = "Количество постов на странице")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Посты получены",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры",
                content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> getPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "count", defaultValue = "5") int count
    ) {
        List<String> paramErrors = postService.validateRequestParams(page, count);

        if (!paramErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(paramErrors));
        }

        return ResponseEntity.ok(postService.getPostDTOs(page, count));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Редактировать пост")
    @SecurityRequirement(name = "JWT")
    @RequestBody(description = "Заголовок, текст и изображение поста",
            content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(
                            requiredProperties = {"Properties"},
                            type = "object",
                            properties = {
                                    @StringToClassMapItem(key = "Image", value = MultipartFile.class),
                                    @StringToClassMapItem(key = "Properties", value = PostPropertiesDTO.class)
                            }
                    ))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пост отредактирован",
                    content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Поста с таким id не существует",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос не от автора поста",
                    content = @Content())
    })
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
    @Operation(summary = "Удалить пост")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пост удален",
                    content = @Content()),
            @ApiResponse(responseCode = "403", description = "Запрос не от автора поста",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Поста с таким id не существует",
                    content = @Content()),
    })
    @Parameter(name = "id", description = "ID поста, который нужно удалить")
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponseDTO(
                    bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList())
            ));
        }
        return null;
    }
}