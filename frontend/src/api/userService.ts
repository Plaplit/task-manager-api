import axiosInstance from './axiosConfig';
import type { User } from '../types/index';

export const userService = {
  getAllUsers: async () => {
    const response = await axiosInstance.get<User[]>('/users');
    return response.data;
  },
  getCurrentUser: async () => {
    const response = await axiosInstance.get<User>('/users/me');
    return response.data;
  }
};
