package com.example.GPS.controller;

import com.example.GPS.dto.request.ApiResponse;
import com.example.GPS.dto.request.UserCreationRequest;
import com.example.GPS.dto.response.UserResponse;
import com.example.GPS.entity.User;
import com.example.GPS.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
}
