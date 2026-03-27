package com.example.GPS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data // Nếu bạn đã cài Lombok, nếu chưa thì viết Getter/Setter nhé
public class GpsData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;      // Khớp với JSON "lat"
    private Double lng;      // Khớp với JSON "lng"
    private Double speed;    // Khớp với JSON "speed"
    private Integer sats;    // Khớp với JSON "sats"

    private LocalDateTime timestamp;
}