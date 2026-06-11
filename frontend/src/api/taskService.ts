import axiosInstance from './axiosConfig';
import type { Task, Page, TaskStatus } from '../types/index';

export const taskService = {
  getProjectTasks: async (projectId: number, status?: TaskStatus, page = 0, size = 20) => {
    let url = `/projects/${projectId}/tasks?page=${page}&size=${size}`;
    if (status) url += `&status=${status}`;
    const response = await axiosInstance.get<Page<Task>>(url);
    return response.data;
  },
  getMyTasks: async (status?: TaskStatus, page = 0, size = 20) => {
    let url = `/tasks/my?page=${page}&size=${size}`;
    if (status) url += `&status=${status}`;
    const response = await axiosInstance.get<Page<Task>>(url);
    return response.data;
  },
  getTask: async (id: number) => {
    const response = await axiosInstance.get<Task>(`/tasks/${id}`);
    return response.data;
  },
  createTask: async (projectId: number, data: any) => {
    const response = await axiosInstance.post<Task>(`/projects/${projectId}/tasks`, data);
    return response.data;
  },
  updateTask: async (id: number, data: any) => {
    const response = await axiosInstance.put<Task>(`/tasks/${id}`, data);
    return response.data;
  },
  deleteTask: async (id: number) => {
    await axiosInstance.delete(`/tasks/${id}`);
  },
  assignToMe: async (id: number) => {
    const response = await axiosInstance.post<Task>(`/tasks/${id}/assign/me`);
    return response.data;
  },
  unassign: async (id: number) => {
    const response = await axiosInstance.delete<Task>(`/tasks/${id}/assign`);
    return response.data;
  }
};
