import axios from 'axios';

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor cho Request: Tự động chèn JWT từ LocalStorage
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor cho Response: Xử lý lỗi tập trung (VD: 401 Unauthorized)
axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.error("Phiên làm việc hết hạn. Vui lòng đăng nhập lại.");
      // Logic logout hoặc redirect ở đây
    }
    return Promise.reject(error);
  }
);

export default axiosClient;