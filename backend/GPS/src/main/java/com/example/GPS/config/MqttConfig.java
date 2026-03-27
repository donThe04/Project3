package com.example.GPS.config;

import com.example.GPS.entity.GpsData;
import com.example.GPS.repository.GpsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

import java.time.LocalDateTime;

@Configuration
@EnableIntegration
public class MqttConfig {

    // 1. Định nghĩa ObjectMapper xử lý được thời gian
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    // 2. Cấu hình kết nối tới HiveMQ Cloud
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "ssl://99688c52187c434599c26e3768a32a74.s1.eu.hivemq.cloud:8883" });
        options.setUserName("dongthe04");
        options.setPassword("Dongvanthe@2606".toCharArray());
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    // 3. ĐÂY LÀ PHẦN QUAN TRỌNG NHẤT: Luồng xử lý dữ liệu tập trung
    @Bean
    public IntegrationFlow mqttInboundFlow(MqttPahoClientFactory mqttClientFactory,
                                           GpsRepository gpsRepository,
                                           ObjectMapper objectMapper) {
        return IntegrationFlow
                // A. Đọc dữ liệu từ Topic của ESP32
                .from(new MqttPahoMessageDrivenChannelAdapter(
                        "SpringBoot_Client_GPS_" + System.currentTimeMillis(),
                        mqttClientFactory,
                        "v1/gps/location"))
                // B. Xử lý dữ liệu nhận được
                .handle(message -> {
                    String payload = (String) message.getPayload();
                    System.out.println("\n--- [Dữ liệu mới từ ESP32] ---");
                    System.out.println("Nội dung: " + payload);

                    try {
                        // Giải mã JSON và lưu vào MySQL
                        GpsData data = objectMapper.readValue(payload, GpsData.class);
                        data.setTimestamp(LocalDateTime.now());

                        gpsRepository.save(data);
                        System.out.println("✅ Đã lưu vào MySQL: [Lat: " + data.getLat() + ", Lng: " + data.getLng() + "]");
                    } catch (Exception e) {
                        System.err.println("❌ Lỗi xử lý JSON hoặc Database: " + e.getMessage());
                    }
                })
                .get();
    }
}