import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { projectService } from '../api/projectService';
import { taskService } from '../api/taskService';
import { userService } from '../api/userService';
import type { Project, Task, User } from '../types';

const ProjectDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const projectId = Number(id);
  const [project, setProject] = useState<Project | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [newTask, setNewTask] = useState({ title: '', description: '', priority: 'MEDIUM', assigneeId: '' });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [projData, tasksData, usersData] = await Promise.all([
          projectService.getProject(projectId),
          taskService.getProjectTasks(projectId),
          userService.getAllUsers()
        ]);
        setProject(projData);
        setTasks(tasksData.content);
        setUsers(usersData);
      } catch (error) {
        console.error('Failed to fetch project details', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [projectId]);

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await taskService.createTask(projectId, {
        ...newTask,
        assigneeId: newTask.assigneeId ? Number(newTask.assigneeId) : null
      });
      setNewTask({ title: '', description: '', priority: 'MEDIUM', assigneeId: '' });
      const updatedTasks = await taskService.getProjectTasks(projectId);
      setTasks(updatedTasks.content);
    } catch (error) {
      console.error('Failed to create task', error);
    }
  };

  if (loading) return <div>Loading project details...</div>;
  if (!project) return <div>Project not found</div>;

  return (
    <div style={{ padding: '20px' }}>
      <Link to="/">Back to Dashboard</Link>
      <h1>{project.name}</h1>
      <p>{project.description}</p>

      <div style={{ marginBottom: '30px', padding: '20px', border: '1px solid #eee' }}>
        <h3>Add Task</h3>
        <form onSubmit={handleCreateTask}>
          <input 
            placeholder="Title" 
            value={newTask.title} 
            onChange={(e) => setNewTask({ ...newTask, title: e.target.value })} 
            required 
          />
          <input 
            placeholder="Description" 
            value={newTask.description} 
            onChange={(e) => setNewTask({ ...newTask, description: e.target.value })} 
          />
          <select 
            value={newTask.priority} 
            onChange={(e) => setNewTask({ ...newTask, priority: e.target.value })}
          >
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
            <option value="CRITICAL">CRITICAL</option>
          </select>
          <select 
            value={newTask.assigneeId} 
            onChange={(e) => setNewTask({ ...newTask, assigneeId: e.target.value })}
          >
            <option value="">Unassigned</option>
            {users.map(u => (
              <option key={u.id} value={u.id}>{u.firstName} {u.lastName}</option>
            ))}
          </select>
          <button type="submit">Add Task</button>
        </form>
      </div>

      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ backgroundColor: '#f4f4f4' }}>
            <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Title</th>
            <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Status</th>
            <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Priority</th>
            <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Assignee</th>
            <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map(task => (
            <tr key={task.id}>
              <td style={{ padding: '10px', border: '1px solid #ddd' }}>{task.title}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd' }}>{task.status}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd' }}>{task.priority}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd' }}>{task.assigneeEmail || 'Unassigned'}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                <Link to={`/tasks/${task.id}`}>Edit</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProjectDetailsPage;
