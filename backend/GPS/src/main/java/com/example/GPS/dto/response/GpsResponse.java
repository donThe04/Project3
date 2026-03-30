package com.example.GPS.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GpsResponse {
    private Double lat;
    private Double lng;
    private Double speed;
    private Integer sats;
    private LocalDateTime timestamp;
}