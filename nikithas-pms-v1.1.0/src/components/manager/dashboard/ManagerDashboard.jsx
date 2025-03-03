import React, { useState, useEffect, useRef } from "react";
import { Chart, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie, getElementsAtEvent } from "react-chartjs-2";
import { Link, useNavigate } from "react-router-dom";
import { Users, NotebookPen, CheckCircle, Bell } from "lucide-react";
import axios from "axios";
import Notification from '../../modal/notification/Notification';
import { NavLink } from 'react-router-dom';

import "./ManagerDashboard.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";

Chart.register(ArcElement, Tooltip, Legend);

const ManagerDashboard = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [managerData, setManagerData] = useState(null);

  const chartRef = useRef(); // <-- Add ref
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/pms/manager/profile",
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        setManagerData(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchData();
  }, []);

  const data = {
    labels: ["Completed", "Pending"],
    datasets: [
      {
        data: [50, 50],
        backgroundColor: ["#4CAF50", "#FF9800"],
        borderWidth: 1,
      },
    ],
  };

  const handleChartClick = (event) => {
    const chart = chartRef.current;
    if (!chart) return;

    const elements = getElementsAtEvent(chart, event);
    if (elements.length > 0) {
      const clickedIndex = elements[0].index;
      if (clickedIndex === 0) {
        navigate("/completed-assessments"); // Completed
      } else if (clickedIndex === 1) {
        navigate("/pending-assessments"); // Pending
      }
    }
  };

  return (
    <div className="dashboard-container">
      <button
        className="sidebar-toggle"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        ☰
      </button>

      <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
        <div className="profile-container">
          <img src={profile} alt="Profile" className="profile-pic" />
          <h2>
            {managerData?.firstName || "First Name"}{" "}
            {managerData?.lastName || "Last Name"}
          </h2>
        </div>
        <ul>
          <li>
            <NavLink
              to="/manager-dashboard"
              className={({ isActive }) => (isActive ? "active" : "")}
            >
              Dashboard
            </NavLink>
          </li>
          <li>
            <Link to={`/my-team/${managerData?.managerId}`}>My Team</Link>
          </li>
          <li>
            <Link to="/manager-profile">My Profile</Link>
          </li>
        </ul>
      </div>

      <div className="main-content">
        <div className="dashboard-header">
          <h1>Manager Dashboard</h1>
          <img src={logo} alt="Logo" className="dashboard-logo" />
          <div className="header-icons">
            <Bell
              className="notification-icon" 
              onClick={() => setNotificationOpen(!notificationOpen)}
            />
            {notificationOpen && (
              <Notification onClose={() => setNotificationOpen(false)} />
            )}
            <Link to="/" className="logout-btn">Logout</Link>
          </div>
        </div>

        <div className="stats-container">
          <div className="stat-card">
            <Users className="stat-icon team-icon" />
            <div className="stat-text">
              <h2>Total Team Members</h2>
              <p>24</p>
            </div>
          </div>
          <div className="stat-card">
            <NotebookPen className="stat-icon pending-icon" />
            <div className="stat-text">
              <h2>Assessments Pending</h2>
              <p>18/24 (75%)</p>
            </div>
          </div>
          <div className="stat-card">
            <CheckCircle className="stat-icon complete-icon" />
            <div className="stat-text">
              <h2>Assessments Complete</h2>
              <p>20/24 (83%)</p>
            </div>
          </div>
        </div>

        <div className="chart-container">
          <h3 className="chart-title">Assessment Status</h3>
          <div className="chart-wrapper">
            <Pie
              ref={chartRef} // <-- Attach ref to Pie
              data={data}
              onClick={handleChartClick} // <-- Pass only event to handler
            />
          </div>
        </div>
      </div>

      {notificationOpen && (
        <Notification onClose={() => setNotificationOpen(false)} />
      )}
    </div>
  );
};

export default ManagerDashboard;
