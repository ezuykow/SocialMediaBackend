package ru.ezuykow.socialmediabackend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ezuykow.socialmediabackend.dto.AuthDTO;
import ru.ezuykow.socialmediabackend.dto.ErrorResponseDTO;
import ru.ezuykow.socialmediabackend.dto.JwtTokenDTO;
import ru.ezuykow.socialmediabackend.dto.UserDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;
import ru.ezuykow.socialmediabackend.security.JWT.JWTUtil;
import ru.ezuykow.socialmediabackend.services.AuthService;

import java.util.Collections;

/**
 * @author ezuykow
 */
@Tag(name = "Регистрация и аутентификация")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Зарегистрировать пользователя", description = "Возвращает токен аутентификации")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован",
                    content = @Content(schema = @Schema(implementation = JwtTokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные регистрационные данные",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDto, BindingResult bindingResult) {

        User user = userMapper.mapUserDtoToUser(userDto);
        authService.checkExistenceAndUsername(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(
                            bindingResult.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .toList())
                    );
        }

        authService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new JwtTokenDTO(token));
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентифицировать пользователя", description = "Возвращает токен аутентификации")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь аутентифицирован",
                    content = @Content(schema = @Schema(implementation = JwtTokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Неверные данные аутентификации",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(
                            bindingResult.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .toList())
                    );
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponseDTO(Collections.singletonList("incorrect credential!")));
        }

        String token = jwtUtil.generateToken(authDto.getUsername());
        return ResponseEntity.ok(new JwtTokenDTO(token));
    }
}
