package com.example.GPS.mapper;

import com.example.GPS.dto.request.UserCreationRequest;
import com.example.GPS.dto.request.UserUpdateRequest;
import com.example.GPS.dto.response.UserResponse;
import com.example.GPS.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
