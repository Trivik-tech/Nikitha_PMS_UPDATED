# Nikitha PMS (Performance Management System)

A comprehensive Performance Management System designed to handle performance appraisals, KRA/KPI assignments, and organizational tracking across multiple roles.

## 🚀 Projects Included

This repository contains both the frontend and backend applications for the Nikitha PMS platform:

1. **`Nikitas-PMS-v1.1.0/` (Backend)**
   - **Tech Stack:** Java, Spring Boot, Spring Security (JWT), Spring Data JPA, MySQL.
   - **Features:** RESTful APIs, Role-based access control, file uploads/processing (Excel), KPI/KRA data management.
   - **Port:** Runs on `http://localhost:8080`

2. **`nikithas-pms-v1.1.0/` (Frontend)**
   - **Tech Stack:** React.js, React Router, Axios, CSS Modules.
   - **Features:** Dedicated dashboards for HR, Managers, and Employees. Form submissions, data tables, and interactive performance tracking.
   - **Port:** Runs on `http://localhost:3000` (or `3001`)

---

## 👥 User Roles & Access

The application is structured around three primary roles, each with designated functionality:

*   **👨‍💼 HR (`HR`)**: Can onboard employees, manage departments, initiate performance review cycles, and view organization-wide metrics.
*   **👔 Manager (`MANAGER`)**: Can view their assigned team, allocate specific KRAs and KPIs to team members, and conduct performance reviews.
*   **🧑‍💻 Employee (`EMPLOYEE`)**: Can view their assigned KRAs/KPIs, submit self-assessments, and track their performance scores.

---

## 🛠️ Setup & Installation

### Prerequisites
- **Java 17+**
- **Node.js (v16+)**
- **MySQL Database** (running locally on default port `3306`)

### 1. Database Configuration
Ensure MySQL is running. The backend is configured to connect to a local database using the default credentials:
- **Username:** `root`
- **Password:** `root`

*(You can update these preferences in `Nikitas-PMS-v1.1.0/src/main/resources/application.yml`)*

### 2. Starting the Backend
From the root of the repository, navigate to the backend directory and run the Spring Boot application:

```bash
cd Nikitas-PMS-v1.1.0
./mvnw.cmd spring-boot:run
```
*(The server will start on port 8080)*

### 3. Starting the Frontend
Open a new terminal, navigate to the frontend directory, install dependencies, and start the React development server:

```bash
cd nikithas-pms-v1.1.0
npm install
npm start
```
*(The UI will launch in your browser at `http://localhost:3000`)*

---

## 🔑 Demo Data & Testing credentials

To quickly test the application without manually registering users, the backend includes a **Dummy Data Seeder** (`CommandLineRunner`). 

Upon startup, if the database is empty, it automatically injects a sample organization structure so you can log in immediately.

You can use the following seeded credentials to explore the application:

| Role | Username | Password |
| :--- | :--- | :--- |
| **HR** | `HR999` | `trivik` |
| **Manager** | `MGR999` | `trivik` |
| **Employee** | `EMP999` | `trivik` |

## 📦 Building for Production

To create a production-ready build of the React frontend:
```bash
cd nikithas-pms-v1.1.0
npm run build
```
This will generate an optimized bundle in the `build/` directory.
