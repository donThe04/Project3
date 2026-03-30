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
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GpsService {

    GpsRepository gpsRepository;

    // 1. Khai báo "Cái loa" để đẩy dữ liệu xuống Frontend qua WebSocket
    SimpMessagingTemplate messagingTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void receiveGpsDataFromMqtt(String payload) {
        System.out.println("🔥 Đã nhận dữ liệu từ ESP32 qua HiveMQ: " + payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

            GpsData newData = new GpsData();
            newData.setLat(jsonNode.get("lat").asDouble());
            newData.setLng(jsonNode.get("lng").asDouble());
            newData.setSpeed(jsonNode.get("speed").asDouble());
            newData.setSats(jsonNode.get("sats").asInt());
            newData.setTimestamp(LocalDateTime.now());

            // Lưu xuống MySQL (Để phục vụ việc xem lại Lịch sử)
            gpsRepository.save(newData);
            System.out.println("✅ Đã lưu tọa độ vào Database thành công!");

            // 2. CHÍNH LÀ ĐÂY: Đẩy dữ liệu này xuống Topic mà Frontend đang lắng nghe
            // "/topic/locations" phải khớp với wsService.subscribe bên Frontend
            messagingTemplate.convertAndSend("/topic/locations", GpsResponse.builder()
                    .lat(newData.getLat())
                    .lng(newData.getLng())
                    .speed(newData.getSpeed())
                    .sats(newData.getSats())
                    .timestamp(newData.getTimestamp())
                    .build());

            System.out.println("🚀 Đã phát tín hiệu Real-time tới Frontend!");

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