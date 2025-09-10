package com.security_service.mapper;

import com.security_service.dto.AuthResponse;
import com.security_service.dto.CreateUserRequest;
import com.security_service.dto.UserResponse;
import com.security_service.entity.Role;
import com.security_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    AuthResponse toAuthResponse(User user, String accessToken, String refreshToken);

    AuthResponse toAuthResponse(UserResponse response, String accessToken, String refreshToken);

    default User toEntity(CreateUserRequest request, Role role) {
        User user = toEntity(request);
        user.setRole(role);

        return user;
    }
}
