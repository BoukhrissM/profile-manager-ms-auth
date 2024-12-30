package com.example.msauth.service;

import com.example.msauth.config.UserAuthenticationProvider;
import com.example.msauth.dto.SignUpDto;
import com.example.msauth.dto.UserDto;
import com.example.msauth.entitiy.User;
import com.example.msauth.exception.AppException;
import com.example.msauth.mapper.UserMapper;
import com.example.msauth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    private final UserAuthenticationProvider userAuthenticationProvider;

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        User savedUser = userRepository.save(user);

        log.info("Creating new user {}", userDto.getEmail());

        return userMapper.toUserDto(savedUser);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public UserDto validateToken(String token) {
        return (UserDto) userAuthenticationProvider.validateToken(token).getPrincipal();
    }
}
