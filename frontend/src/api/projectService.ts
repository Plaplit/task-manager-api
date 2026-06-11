import axiosInstance from './axiosConfig';
import type { Page, Project } from '../types/index';

export const projectService = {
  getProjects: async (page = 0, size = 10) => {
    const response = await axiosInstance.get<Page<Project>>(`/projects?page=${page}&size=${size}`);
    return response.data;
  },
  getProject: async (id: number) => {
    const response = await axiosInstance.get<Project>(`/projects/${id}`);
    return response.data;
  },
  createProject: async (data: { name: string; description?: string }) => {
    const response = await axiosInstance.post<Project>('/projects', data);
    return response.data;
  },
  updateProject: async (id: number, data: any) => {
    const response = await axiosInstance.put<Project>(`/projects/${id}`, data);
    return response.data;
  },
  deleteProject: async (id: number) => {
    await axiosInstance.delete(`/projects/${id}`);
  }
};
