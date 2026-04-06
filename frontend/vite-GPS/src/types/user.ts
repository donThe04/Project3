export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  code: number;
  result: {
    authenticated: boolean;
    token?: string; // Tùy thuộc vào backend Spring Boot của bạn có trả về token không
  };
  message?: string;
}