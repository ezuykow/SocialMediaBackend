package ru.ezuykow.socialmediabackend.controllers;

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
import ru.ezuykow.socialmediabackend.dto.UserDTO;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.UserMapper;
import ru.ezuykow.socialmediabackend.security.JWT.JWTUtil;
import ru.ezuykow.socialmediabackend.services.AuthService;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDto, BindingResult bindingResult) {

        User user = userMapper.mapUserDtoToUser(userDto);
        authService.checkExistenceAndUsername(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors",
                            bindingResult.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList()))
                    );
        }

        authService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of("jwt_token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authDto, BindingResult bindingResult ) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors",
                            bindingResult.getAllErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.toList()))
                    );
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "incorrect credential!"));
        }

        String token = jwtUtil.generateToken(authDto.getUsername());
        return ResponseEntity.ok(Map.of("jwt_token", token));
    }
}
