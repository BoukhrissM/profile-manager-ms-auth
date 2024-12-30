package com.example.msauth.mapper;

import com.example.msauth.dto.SignUpDto;
import com.example.msauth.dto.UserDto;
import com.example.msauth.entitiy.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
