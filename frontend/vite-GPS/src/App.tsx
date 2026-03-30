import { useEffect, useState } from 'react';
import axiosClient from './api/axiosClient';
import { wsService } from './services/websocketService';
import type { GpsData } from './types/gps';
import GpsMapLeaflet from './components/GpsMapLeaflet';

function App() {
  const [location, setLocation] = useState<GpsData>({
    lat: 21.0285, // Mặc định Hà Nội
    lng: 105.8542,
    speed: 0,
    sats: 0,
    timestamp: new Date().toISOString(),
  });

  useEffect(() => {
    // 1. Lấy tọa độ mới nhất qua API REST khi khởi động
    const fetchLatest = async () => {
      try {
        const response = await axiosClient.get<GpsData>('/latest');
        if (response.data) {
          setLocation(response.data);
        }
      } catch (error) {
        console.error("Lỗi lấy dữ liệu API:", error);
      }
    };

    fetchLatest();

    // 2. Lắng nghe cập nhật Real-time qua WebSocket
    wsService.connect((newData) => {
      console.log("📍 Tọa độ mới từ ESP32:", newData);
      setLocation(newData);
    });

    return () => wsService.disconnect();
  }, []);

  return (
    <div style={{ maxWidth: '1000px', margin: '0 auto', padding: '20px', fontFamily: 'Arial' }}>
      <header style={{ textAlign: 'center', marginBottom: '30px' }}>
        <h1 style={{ color: '#2c3e50' }}>Hệ thống Giám sát GPS Real-time</h1>
        <p style={{ color: '#7f8c8d' }}>Stack: React + TS + Spring Boot + ESP32</p>
      </header>

      <main style={{ display: 'grid', gridTemplateColumns: '1fr 3fr', gap: '20px' }}>
        {/* Panel thông tin */}
        <section style={{ padding: '20px', background: '#ecf0f1', borderRadius: '12px' }}>
          <h2 style={{ fontSize: '1.2rem', borderBottom: '1px solid #bdc3c7', paddingBottom: '10px' }}>
            Trạng thái
          </h2>
          <div style={{ marginTop: '15px' }}>
            <p>🛰️ <strong>Vệ tinh:</strong> {location.sats}</p>
            <p>🏎️ <strong>Tốc độ:</strong> {location.speed} km/h</p>
            <p>📅 <strong>Cập nhật:</strong> <br />
              <small>{new Date(location.timestamp).toLocaleString()}</small>
            </p>
          </div>
          <div style={{ marginTop: '20px', fontSize: '0.8rem', color: '#95a5a6' }}>
            Lat: {location.lat.toFixed(6)} <br />
            Lng: {location.lng.toFixed(6)}
          </div>
        </section>

        {/* Bản đồ */}
        <section>
          <GpsMapLeaflet location={{ lat: location.lat, lng: location.lng }} />
        </section>
      </main>
    </div>
  );
}

export default App;