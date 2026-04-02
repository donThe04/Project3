package com.example.GPS.service;

import com.example.GPS.dto.request.UserCreationRequest;
import com.example.GPS.dto.response.UserResponse;
import com.example.GPS.entity.User;
import com.example.GPS.exception.AppException;
import com.example.GPS.exception.ErrorCode;
import com.example.GPS.mapper.UserMapper;
import com.example.GPS.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // tao 1 user moi

    public UserResponse createUser(UserCreationRequest request) { // Đổi kiểu trả về thành UserResponse
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lưu và chuyển đổi ngược lại thành Response để trả về
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
