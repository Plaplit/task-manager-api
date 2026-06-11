import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { taskService } from '../api/taskService';
import { userService } from '../api/userService';
import type { Task, User } from '../types';

const TaskDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const taskId = Number(id);
  const navigate = useNavigate();
  const [task, setTask] = useState<Task | null>(null);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [taskData, usersData] = await Promise.all([
          taskService.getTask(taskId),
          userService.getAllUsers()
        ]);
        setTask(taskData);
        setUsers(usersData);
      } catch (error) {
        console.error('Failed to fetch task details', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [taskId]);

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!task) return;
    try {
      await taskService.updateTask(taskId, {
        title: task.title,
        description: task.description,
        status: task.status,
        priority: task.priority,
        assigneeId: task.assigneeId
      });
      navigate(`/projects/${task.projectId}`);
    } catch (error) {
      console.error('Failed to update task', error);
    }
  };

  const handleDelete = async () => {
    if (!task) return;
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskService.deleteTask(taskId);
        navigate(`/projects/${task.projectId}`);
      } catch (error) {
        console.error('Failed to delete task', error);
      }
    }
  };

  if (loading) return <div>Loading task details...</div>;
  if (!task) return <div>Task not found</div>;

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <Link to={`/projects/${task.projectId}`}>Back to Project</Link>
      <h1>Edit Task</h1>
      <form onSubmit={handleUpdate}>
        <div style={{ marginBottom: '10px' }}>
          <label>Title:</label>
          <input 
            value={task.title} 
            onChange={(e) => setTask({ ...task, title: e.target.value })} 
            required 
            style={{ width: '100%' }}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Description:</label>
          <textarea 
            value={task.description || ''} 
            onChange={(e) => setTask({ ...task, description: e.target.value })} 
            style={{ width: '100%', height: '100px' }}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Status:</label>
          <select 
            value={task.status} 
            onChange={(e) => setTask({ ...task, status: e.target.value as any })}
            style={{ width: '100%' }}
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="IN_REVIEW">IN_REVIEW</option>
            <option value="DONE">DONE</option>
          </select>
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Priority:</label>
          <select 
            value={task.priority} 
            onChange={(e) => setTask({ ...task, priority: e.target.value as any })}
            style={{ width: '100%' }}
          >
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
            <option value="CRITICAL">CRITICAL</option>
          </select>
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Assignee:</label>
          <select 
            value={task.assigneeId || ''} 
            onChange={(e) => setTask({ ...task, assigneeId: e.target.value ? Number(e.target.value) : null })}
            style={{ width: '100%' }}
          >
            <option value="">Unassigned</option>
            {users.map(u => (
              <option key={u.id} value={u.id}>{u.firstName} {u.lastName}</option>
            ))}
          </select>
        </div>
        <div style={{ marginTop: '20px', display: 'flex', gap: '10px' }}>
          <button type="submit" style={{ padding: '10px 20px', backgroundColor: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px' }}>
            Save Changes
          </button>
          <button type="button" onClick={async () => {
            await taskService.assignToMe(taskId);
            const updated = await taskService.getTask(taskId);
            setTask(updated);
          }} style={{ padding: '10px 20px', backgroundColor: '#2196F3', color: 'white', border: 'none', borderRadius: '4px' }}>
            Assign to Me
          </button>
          <button type="button" onClick={async () => {
            await taskService.unassign(taskId);
            const updated = await taskService.getTask(taskId);
            setTask(updated);
          }} style={{ padding: '10px 20px', backgroundColor: '#FF9800', color: 'white', border: 'none', borderRadius: '4px' }}>
            Unassign
          </button>
          <button type="button" onClick={handleDelete} style={{ padding: '10px 20px', backgroundColor: '#f44336', color: 'white', border: 'none', borderRadius: '4px' }}>
            Delete Task
          </button>
        </div>
      </form>
    </div>
  );
};

export default TaskDetailsPage;
