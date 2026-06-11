import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ProjectDetailsPage from './pages/ProjectDetailsPage';
import TaskDetailsPage from './pages/TaskDetailsPage';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
};

const Navbar: React.FC = () => {
  const { user, logout, isAuthenticated } = useAuth();
  if (!isAuthenticated) return null;

  return (
    <nav style={{ padding: '10px 20px', backgroundColor: '#333', color: 'white', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <div>
        <strong>Task Manager</strong>
      </div>
      <div>
        <span style={{ marginRight: '20px' }}>Welcome, {user?.firstName}</span>
        <button onClick={logout} style={{ padding: '5px 10px' }}>Logout</button>
      </div>
    </nav>
  );
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route 
            path="/" 
            element={
              <PrivateRoute>
                <DashboardPage />
              </PrivateRoute>
            } 
          />
          <Route 
            path="/projects/:id" 
            element={
              <PrivateRoute>
                <ProjectDetailsPage />
              </PrivateRoute>
            } 
          />
          <Route 
            path="/tasks/:id" 
            element={
              <PrivateRoute>
                <TaskDetailsPage />
              </PrivateRoute>
            } 
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
};

export default App;
