package com.example.GPS.controller;


import com.example.GPS.dto.response.GpsResponse;
import com.example.GPS.service.GpsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GpsController {

    GpsService gpsService;

    // 1. Lấy vị trí mới nhất
    @GetMapping("/latest")
    public GpsResponse getLatest() {
        return gpsService.getLatestLocation();
    }

    // 2. Lấy lịch sử di chuyển theo khoảng thời gian
    // Sử dụng @RequestParam để truyền startTime và endTime lên URL
    @GetMapping("/history")
    public List<GpsResponse> getHistory(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return gpsService.getHistoryByTime(startTime, endTime);
    }
}