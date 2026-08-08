import { createContext, useContext, useState } from 'react';
import axiosClient from '../api/axiosClient.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('movio_user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = async (email, password) => {
    const res = await axiosClient.post('/auth/login', { email, password });
    const { token, name, email: userEmail, role } = res.data;
    localStorage.setItem('movio_token', token);
    localStorage.setItem('movio_user', JSON.stringify({ name, email: userEmail, role }));
    setUser({ name, email: userEmail, role });
  };

  const register = async (name, email, password) => {
    const res = await axiosClient.post('/auth/register', { name, email, password });
    const { token, name: userName, email: userEmail, role } = res.data;
    localStorage.setItem('movio_token', token);
    localStorage.setItem('movio_user', JSON.stringify({ name: userName, email: userEmail, role }));
    setUser({ name: userName, email: userEmail, role });
  };

  const logout = () => {
    localStorage.removeItem('movio_token');
    localStorage.removeItem('movio_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}