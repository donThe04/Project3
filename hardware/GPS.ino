#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <TinyGPS++.h>
#include <HardwareSerial.h>

// 1. --- CẤU HÌNH MẠNG ---
const char* ssid = "12 Chinh Kinh";
const char* password = "T01082025";

// 2. --- CẤU HÌNH HIVEMQ CLOUD (SỬA LẠI ĐÚNG ĐỊNH DẠNG) ---
const char* mqtt_server = "99688c52187c434599c26e3768a32a74.s1.eu.hivemq.cloud"; 
const int mqtt_port = 8883;
const char* mqtt_user = "dongthe04"; 
const char* mqtt_pass = "Dongvanthe@2606";
const char* mqtt_topic = "v1/gps/location";

// 3. --- KHỞI TẠO ĐỐI TƯỢNG ---
WiFiClientSecure espClient;
PubSubClient client(espClient);
TinyGPSPlus gps;
HardwareSerial SerialGPS(2); // RX2=16, TX2=17

unsigned long lastSend = 0;

void setup_wifi() {
  Serial.print("\nKet noi WiFi: ");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\nWiFi Connected!");
  espClient.setInsecure(); // Bo qua xac thuc chung chi SSL cho nhanh
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Dang ket noi HiveMQ...");
    String clientId = "ESP32_GPS_" + String(random(0xffff), HEX);
    if (client.connect(clientId.c_str(), mqtt_user, mqtt_pass)) {
      Serial.println("CONNECTED!");
    } else {
      Serial.print("Loi rc="); Serial.print(client.state());
      delay(5000);
    }
  }
}

void setup() {
  Serial.begin(115200);
  SerialGPS.begin(9600, SERIAL_8N1, 16, 17);
  
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  
  Serial.println("--- He thong GPS MQTT da bat dau ---");
}

void loop() {
  // Duy tri ket noi MQTT
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // Doc du lieu tu GPS
  while (SerialGPS.available() > 0) {
    gps.encode(SerialGPS.read());
  }

  // Gui du lieu len MQTT moi 5 giay
  if (millis() - lastSend > 5000) {
    lastSend = millis();
    
    if (gps.location.isValid()) {
      // Tao chuoi JSON
      String payload = "{";
      payload += "\"lat\":" + String(gps.location.lat(), 6) + ",";
      payload += "\"lng\":" + String(gps.location.lng(), 6) + ",";
      payload += "\"speed\":" + String(gps.speed.kmph(), 2) + ",";
      payload += "\"sats\":" + String(gps.satellites.value());
      payload += "}";

      Serial.print("Publishing: "); Serial.println(payload);
      client.publish(mqtt_topic, payload.c_str());
      
      // In ra Serial Monitor de theo doi tai cho
      displayInfo(); 
    } else {
      Serial.println("GPS dang tim tin hieu...");
    }
  }
}

void displayInfo() {
  Serial.print(F("Vi tri: ")); 
  Serial.print(gps.location.lat(), 6);
  Serial.print(F(","));
  Serial.print(gps.location.lng(), 6);
  Serial.print(F("  Ngay: "));
  Serial.print(gps.date.day()); Serial.print("/");
  Serial.print(gps.date.month()); Serial.print("/");
  Serial.println(gps.date.year());
}