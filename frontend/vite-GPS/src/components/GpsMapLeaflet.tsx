import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix lỗi icon Marker bị biến mất trong React Leaflet
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

interface Props {
  location: { lat: number; lng: number };
}

// Component phụ giúp bản đồ tự động di chuyển tâm theo Marker
const RecenterMap = ({ lat, lng }: { lat: number, lng: number }) => {
    const map = useMap();
    useEffect(() => {
        map.setView([lat, lng], map.getZoom());
    }, [lat, lng, map]);
    return null;
};

const GpsMapLeaflet: React.FC<Props> = ({ location }) => {
  return (
    <MapContainer 
      center={[location.lat, location.lng]} 
      zoom={15} 
      style={{ height: '500px', width: '100%', borderRadius: '12px' }}
    >
      {/* Lớp nền bản đồ miễn phí từ OpenStreetMap */}
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />
      
      {/* Hiển thị vị trí thiết bị */}
      <Marker position={[location.lat, location.lng]}>
        <Popup>
          Vị trí hiện tại của ESP32 <br /> 
          Tọa độ: {location.lat}, {location.lng}
        </Popup>
      </Marker>

      {/* Tự động di chuyển tâm bản đồ */}
      <RecenterMap lat={location.lat} lng={location.lng} />
    </MapContainer>
  );
};

export default GpsMapLeaflet;