package com.example.GPS.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.PrePersist;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String email;
    String role;
    String created_at;

    // Tự động gán thời gian trước khi lưu vào Database
    @PrePersist
    protected void onCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.created_at = LocalDateTime.now().format(formatter);
    }
}
