import axiosClient from './axiosClient';
import type { LoginRequest, RegisterRequest, AuthResponse } from '../types/user';

export const loginApi = (data: LoginRequest) => {
  // Thay đổi endpoint '/auth/login' cho khớp với Controller Spring Boot của Thế nhé
  return axiosClient.post<AuthResponse>('/auth/log-in', data);
};

export const registerApi = (data: RegisterRequest) => {
  return axiosClient.post<AuthResponse>('/users', data);
};