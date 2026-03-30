package com.example.GPS.service;

import com.example.GPS.dto.response.GpsResponse;
import com.example.GPS.entity.GpsData;
import com.example.GPS.repository.GpsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GpsService {

    GpsRepository gpsRepository;

    // Tự khởi tạo ObjectMapper ngay tại đây thay vì chờ Spring Inject
    ObjectMapper objectMapper = new ObjectMapper();

    // =========================================================================
    // 1. LẮNG NGHE MQTT VÀ LƯU VÀO DATABASE
    // =========================================================================
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void receiveGpsDataFromMqtt(String payload) {
        System.out.println("🔥 Đã nhận dữ liệu từ ESP32 qua HiveMQ: " + payload);

        try {
            // Đọc chuỗi JSON
            JsonNode jsonNode = objectMapper.readTree(payload);

            // Tạo entity mới để lưu database
            GpsData newData = new GpsData();
            newData.setLat(jsonNode.get("lat").asDouble());
            newData.setLng(jsonNode.get("lng").asDouble());
            newData.setSpeed(jsonNode.get("speed").asDouble());
            newData.setSats(jsonNode.get("sats").asInt());

            // Gán thời gian hiện tại
            newData.setTimestamp(LocalDateTime.now());

            // Lưu xuống MySQL
            gpsRepository.save(newData);
            System.out.println("✅ Đã lưu tọa độ vào Database thành công!");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xử lý dữ liệu MQTT: " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. PHỤC VỤ API CONTROLLER
    // =========================================================================

    // Lấy lịch sử theo khoảng thời gian
    public List<GpsResponse> getHistoryByTime(LocalDateTime startTime, LocalDateTime endTime) {
        List<GpsData> dataList = gpsRepository.findByTimestampBetweenOrderByTimestampDesc(startTime, endTime);

        return dataList.stream().map(data -> GpsResponse.builder()
                .lat(data.getLat())
                .lng(data.getLng())
                .speed(data.getSpeed())
                .sats(data.getSats())
                .timestamp(data.getTimestamp())
                .build()
        ).collect(Collectors.toList());
    }

    // Lấy vị trí mới nhất
    public GpsResponse getLatestLocation() {
        GpsData latest = gpsRepository.findTopByOrderByTimestampDesc();
        if (latest == null) {
            return null;
        }
        return GpsResponse.builder()
                .lat(latest.getLat())
                .lng(latest.getLng())
                .speed(latest.getSpeed())
                .sats(latest.getSats())
                .timestamp(latest.getTimestamp())
                .build();
    }
}