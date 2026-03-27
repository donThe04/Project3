# Project3
# 🛰️ ESP32 GPS Tracking System

Dự án theo dõi tọa độ thời gian thực sử dụng chip **ESP32**, module **GPS**, Backend **Spring Boot** và Frontend **TypeScript**.

---

## 🚀 Kiến trúc hệ thống
* **Hardware:** ESP32 + Module GPS (Neo-6M) đọc dữ liệu tọa độ qua UART.
* **Backend:** Java Spring Boot nhận dữ liệu qua REST API và lưu trữ.
* **Frontend:** HTML/CSS & TypeScript hiển thị vị trí trên bản đồ (Leaflet/Google Maps).

---

## 📂 Cấu trúc thư mục
* `/hardware`: Mã nguồn C++ cho ESP32 (Arduino IDE/PlatformIO).
* `/backend`: Mã nguồn Java Spring Boot.
* `/frontend`: Mã nguồn giao diện người dùng (TypeScript).

---

## 🛠️ Công nghệ sử dụng
* **Ngôn ngữ:** C++, Java, TypeScript.
* **Framework:** Spring Boot, Web Starter, JPA.
* **Phần cứng:** ESP32 DevKit V1, Module GPS Neo-6M.

---

## 📋 Hướng dẫn cài đặt sơ bộ

### 1. Phần cứng (Hardware)
* Kết nối chân TX của GPS vào RX của ESP32 (và ngược lại).
* Cấu hình Wi-Fi và URL của Backend trong code ESP32.

### 2. Backend (Spring Boot)
* Yêu cầu: Java 17+, Maven.
* Chạy lệnh: `mvn spring-boot:run`

### 3. Frontend
* Mở file `index.html` hoặc chạy môi trường dev của TypeScript.

---

## 👨‍💻 Tác giả
* **Đồng Văn Thế**
* Dự án đang trong quá trình phát triển.
