import axiosInstance from './axiosConfig';
import type { AuthResponse } from '../types/index';


export const authService = {
  register: async (data: any) => {
    const response = await axiosInstance.post<AuthResponse>('/auth/register', data);
    return response.data;
  },
  login: async (data: any) => {
    const response = await axiosInstance.post<AuthResponse>('/auth/login', data);
    return response.data;
  },
  logout: () => {
    localStorage.removeItem('token');
  }
};
