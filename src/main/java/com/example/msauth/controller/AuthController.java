package com.example.msauth.controller;

import com.example.msauth.config.UserAuthenticationProvider;
import com.example.msauth.dto.SignUpDto;
import com.example.msauth.dto.UserDto;
import com.example.msauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> authenticate(@AuthenticationPrincipal UserDto user) throws Exception {

        user.setToken(userAuthenticationProvider.createToken(user.getEmail()));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.signUp(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId() + "/profile")).body(createdUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> signOut() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<UserDto> validateToken(@RequestParam("token") String token) {
        UserDto user = userService.validateToken(token);
        return ResponseEntity.created(URI.create("/users/" + user.getId() + "/profile")).body(user);
    }

}
