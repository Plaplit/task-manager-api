import React, { useEffect, useState } from 'react';
import { projectService } from '../api/projectService';
import type { Project } from '../types/index';
import { Link } from 'react-router-dom';

const DashboardPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [newProjectName, setNewProjectName] = useState('');
  const [newProjectDesc, setNewProjectDesc] = useState('');

  const fetchProjects = async () => {
    try {
      const data = await projectService.getProjects();
      setProjects(data.content);
    } catch (error) {
      console.error('Failed to fetch projects', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProjects();
  }, []);

  const handleCreateProject = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await projectService.createProject({ name: newProjectName, description: newProjectDesc });
      setNewProjectName('');
      setNewProjectDesc('');
      fetchProjects();
    } catch (error) {
      console.error('Failed to create project', error);
    }
  };

  const handleDeleteProject = async (projectId: number) => {
    if (!window.confirm('Na pewno chcesz usunąć ten projekt?')) {
      return;
    }

    try {
      await projectService.deleteProject(projectId);
      await fetchProjects();
    } catch (error) {
      console.error('Failed to delete project', error);
    }
  };

  if (loading) return <div>Loading projects...</div>;

  return (
    <div style={{ padding: '20px' }}>
      <h1>My Projects</h1>
      
      <div style={{ marginBottom: '30px', padding: '20px', border: '1px solid #eee' }}>
        <h3>Create New Project</h3>
        <form onSubmit={handleCreateProject}>
          <input 
            placeholder="Project Name" 
            value={newProjectName} 
            onChange={(e) => setNewProjectName(e.target.value)} 
            required 
            style={{ marginRight: '10px' }}
          />
          <input 
            placeholder="Description" 
            value={newProjectDesc} 
            onChange={(e) => setNewProjectDesc(e.target.value)} 
            style={{ marginRight: '10px' }}
          />
          <button type="submit">Create</button>
        </form>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' }}>
        {projects.map((project) => (
          <div key={project.id} style={{ padding: '15px', border: '1px solid #ccc', borderRadius: '8px' }}>
            <h3>{project.name}</h3>
            <p>{project.description}</p>
            <p>Status: {project.status}</p>
            <p>Tasks: {project.taskCount}</p>
            <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
              <Link to={`/projects/${project.id}`}>View Tasks</Link>
              <button
                type="button"
                onClick={() => handleDeleteProject(project.id)}
                style={{ color: 'crimson', borderColor: 'crimson', background: 'white', cursor: 'pointer' }}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default DashboardPage;
