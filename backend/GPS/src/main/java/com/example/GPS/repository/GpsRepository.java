package com.example.GPS.repository;

import com.example.GPS.entity.GpsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GpsRepository extends JpaRepository<GpsData, Long> {
    // Lấy danh sách tọa độ trong khoảng thời gian, sắp xếp mới nhất lên đầu
    List<GpsData> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startTime, LocalDateTime endTime);

    // Hàm này để lấy vị trí mới nhất (dùng cho realtime nếu cần gọi API)
    GpsData findTopByOrderByTimestampDesc();
}
