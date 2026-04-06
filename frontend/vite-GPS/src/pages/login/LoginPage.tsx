import React, { useState } from 'react';
import { loginApi } from '../../api/auth.api';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await loginApi(credentials);
            // Cập nhật logic check response cho chuẩn với interface mới
            if (response.data.result?.authenticated) {
                alert("Đăng nhập thành công!");
                localStorage.setItem('isLoggedIn', 'true');
                navigate('/dashboard'); 
            } else {
                alert("Sai mật khẩu!");
            }
        } catch (error: any) {
            alert("Tài khoản không tồn tại hoặc lỗi kết nối!");
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <form onSubmit={handleLogin} className="w-96 rounded-lg bg-white p-8 shadow-md">
                <h2 className="mb-6 text-2xl font-bold text-center text-blue-600">Hệ Thống GPS</h2>
                <input 
                    type="text" placeholder="Username" required
                    className="mb-4 w-full rounded border p-2 outline-none focus:border-blue-500"
                    onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                />
                <input 
                    type="password" placeholder="Password" required
                    className="mb-6 w-full rounded border p-2 outline-none focus:border-blue-500"
                    onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                />
                <button type="submit" className="w-full rounded bg-blue-500 py-2 text-white hover:bg-blue-600 transition">
                    Đăng Nhập
                </button>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Chưa có tài khoản? <span onClick={() => navigate('/register')} className="cursor-pointer text-blue-500 underline">Đăng ký ngay</span>
                </p>
            </form>
        </div>
    );
};

export default LoginPage;