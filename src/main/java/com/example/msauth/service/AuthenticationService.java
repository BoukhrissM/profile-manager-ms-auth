package com.example.msauth.service;

import com.example.msauth.dto.CredentialsDto;
import com.example.msauth.dto.UserDto;
import com.example.msauth.entitiy.User;
import com.example.msauth.exception.AppException;
import com.example.msauth.mapper.UserMapper;
import com.example.msauth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto authenticate(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmailIgnoreCase(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            log.debug("User {} authenticated correctly", credentialsDto.getEmail());
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException("Login not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
}
