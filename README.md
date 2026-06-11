# Task Manager

A simple fullstack task management application created to learn and demonstrate Java backend development with a basic React frontend.

The main goal of this project is to practice building REST APIs with Spring Boot, securing them with JWT authentication, connecting the backend with a PostgreSQL database, and integrating the API with a frontend application.

The frontend is intentionally simple and demo-oriented. Its purpose is to make testing the backend easier and more user-friendly.

## Project Goal

* practice working with Java and Spring Boot,
* learn how to build REST APIs,
* implement authentication and authorization using JWT,
* connect the application with a PostgreSQL database,
* practice my skills learnt during university,
* develop practical programming skills through hands-on work.

## Project Status

This project is still in development. Some features may be incomplete or subject to change.

The backend is the main part of the project, while the frontend is a lightweight interface created by Ai mainly for testing and demonstration purposes.

## Technologies

### Backend

* Java 17
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* PostgreSQL
* Maven
* Swagger
* Docker

### Frontend

* React
* TypeScript
* Vite
* Axios
* React Router

## Requirements

Before running the project, make sure you have installed:

* Java 17 or newer,
* Maven,
* Node.js and npm,
* Docker Desktop,
* Git.

You can check installed versions with:

```bash
java -version
mvn -version
node -v
npm -v
docker -v
```

## Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/Plaplit/task-manager-api.git
cd task-manager
```

---

### 2. Start Docker

Make sure Docker Desktop is running.

Then start the database container from the project root:

```bash
docker compose up -d
```

If your system uses the older Docker Compose command, use:

```bash
docker-compose up -d
```

To check if the container is running:

```bash
docker ps
```

---

### 3. Start the backend

Backend is contenarized and will start automatically with ```docker-compose```

### 4. Start the frontend

Open a second terminal window.

Go to the frontend folder:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the frontend development server:

```bash
npm run dev
```

The frontend will be available at:

```text
http://localhost:5173
```

---

## How to Verify the Application

1. Start Docker.
2. Start the backend.
3. Start the frontend.
4. Open the frontend in your browser:

```text
http://localhost:5173
```

5. Create a new account using the registration form.
6. Log in with the created account.
7. Test basic features such as:

   * viewing projects,
   * creating projects,
   * viewing project details,
   * managing tasks.

You can also test the API directly in Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Authentication

The application uses JWT authentication.

After successful login, the backend returns a JWT token. The frontend stores this token in local storage and sends it with protected API requests using the `Authorization` header:

```text
Authorization: Bearer <token>
```

Public endpoints:

```text
POST /api/auth/register
POST /api/auth/login
```

Protected endpoints require a valid JWT token.

## Useful API Endpoints

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
```

### Projects

```text
GET    /api/projects
POST   /api/projects
GET    /api/projects/{id}
PUT    /api/projects/{id}
DELETE /api/projects/{id}
```

### Tasks

```text
GET    /api/projects/{projectId}/tasks
POST   /api/projects/{projectId}/tasks
GET    /api/tasks/{taskId}
PUT    /api/tasks/{taskId}
DELETE /api/tasks/{taskId}
GET    /api/tasks/my
```

## Stopping the Project

To stop the frontend, press:

```text
CTRL + C
```

To stop the backend, press:

```text
CTRL + C
```

To stop Docker containers, run from the project root:

```bash
docker compose down
```

or:

```bash
docker-compose down
```

## Common Problems

### Backend does not start

Make sure Docker Desktop is running and the PostgreSQL container is active:

```bash
docker ps
```

If needed, restart the container:

```bash
docker compose down
docker compose up -d
```
---

### npm command does not work

Make sure Node.js and npm are installed:

```bash
node -v
npm -v
```

If dependencies are missing, run inside the frontend folder:

```bash
npm install
```

## Notes

* The frontend is intentionally simple and clear.
* The main focus of the project is backend development.
* The project is created for learning and portfolio purposes.
* The application is not yet production-ready.
* More features and improvements may be added in the future.
