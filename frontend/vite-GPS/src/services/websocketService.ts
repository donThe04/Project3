import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { GpsData } from '../types/gps';

class WebSocketService {
  private client: Client | null = null;

  connect(onMessageReceived: (data: GpsData) => void) {
    this.client = new Client({
      webSocketFactory: () => new SockJS(import.meta.env.VITE_WS_URL),
      debug: (msg) => console.log('STOMP:', msg),
      reconnectDelay: 5000,
    });

    this.client.onConnect = () => {
      this.client?.subscribe('/topic/locations', (message) => {
        if (message.body) {
          const data: GpsData = JSON.parse(message.body);
          onMessageReceived(data);
        }
      });
    };

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }
}

// LỖI CỦA BẠN NẰM Ở ĐÂY: Phải có dòng export này
export const wsService = new WebSocketService();