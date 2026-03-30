export interface GpsData {
  id?: number;
  lat: number;
  lng: number;
  speed: number;
  sats: number;
  timestamp: string;
}

export interface MapLocation {
  lat: number;
  lng: number;
}