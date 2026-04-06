import React, { useState } from 'react';
import { registerApi } from '../../api/auth.api';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [formData, setFormData] = useState({ username: '', email: '' , password: ''});
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await registerApi(formData);
            if (response.status === 200 || response.data.code === 1000) {
                alert("Đăng ký thành công!");
                navigate('/login');
            }
        } catch (error: any) {
            alert(error.response?.data?.message || "Đăng ký thất bại");
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <form onSubmit={handleSubmit} className="w-96 rounded-lg bg-white p-8 shadow-md">
                <h2 className="mb-6 text-2xl font-bold text-center">Đăng Ký Tài Khoản</h2>
                <input 
                    type="text" placeholder="Username" required
                    className="mb-4 w-full rounded border p-2 outline-none focus:border-blue-500"
                    onChange={(e) => setFormData({...formData, username: e.target.value})}
                />
                <input 
                    type="password" placeholder="Password" required
                    className="mb-6 w-full rounded border p-2 outline-none focus:border-blue-500"
                    onChange={(e) => setFormData({...formData, password: e.target.value})}
                />
                <input 
                    type="email" placeholder="Email" required
                    className="mb-4 w-full rounded border p-2 outline-none focus:border-blue-500"
                    onChange={(e) => setFormData({...formData, email: e.target.value})}
                />
                <button type="submit" className="w-full rounded bg-green-500 py-2 text-white hover:bg-green-600 transition">
                    Đăng Ký
                </button>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Đã có tài khoản? <span onClick={() => navigate('/login')} className="cursor-pointer text-blue-500 underline">Đăng nhập</span>
                </p>
            </form>
        </div>
    );
};

export default RegisterPage;