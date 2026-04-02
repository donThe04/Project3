package com.example.GPS.repository;

import com.example.GPS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username); // tu dong sinh cau lenh truy van SQL de kiem tra title co ton tai trong database hay khong

    Optional<User> findByUsername(String Username);
}
