import React, { useState, useEffect, useRef, useCallback } from "react";
import { Chart, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie, getElementsAtEvent } from "react-chartjs-2";
import { Link, useNavigate, NavLink } from "react-router-dom";
import { Users, NotebookPen, CheckCircle, Bell } from "lucide-react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";

import Notification from "../../modal/notification/Notification";
import "./ManagerDashboard.css";
import "./ResponsiveManager.css";

import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import { baseUrl } from "../../urls/CommenUrl";

Chart.register(ArcElement, Tooltip, Legend);

const ManagerDashboard = () => {
  const navigate = useNavigate();
  const chartRef = useRef();

  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0);
  const [newAndUndelivered, setNewAndUndelivered] = useState([]);
  const [managerData, setManagerData] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [teamSize, setTeamSize] = useState(0);
  const [assessmentCount, setAssessmentCount] = useState({
    completed: 0,
    pending: 0,
  });
  const [percentageData, setPercentageData] = useState({
    completedPercentage: 0,
    pendingPercentage: 0,
  });
  const [managerId, setManagerId] = useState("");

  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      localStorage.clear();
      navigate("/");
    }
  }, [token, navigate]);

  const getPercentage = useCallback(async (id) => {
    try {
      const res = await axios.get(
        `${baseUrl}/api/v1/pms/manager/percentage/${id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setPercentageData(res.data);
    } catch (error) { }
  }, [token]);

  const getProfile = useCallback(async () => {
    try {
      const res = await axios.get(`${baseUrl}/api/v1/pms/manager/profile`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const id = res.data.managerId;
      setManagerId(id);
      setManagerData(res.data);
      await getPercentage(id);
    } catch (error) {
      if (error.response?.status === 401) {
        localStorage.clear();
        navigate("/");
      }
    }
  }, [token, navigate, getPercentage]);

  useEffect(() => {
    if (token) getProfile();
  }, [token, getProfile]);

  /* Removed getPercentage definition from here as it was moved up */

  const loadTeamSize = useCallback(async (id) => {
    try {
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/manager/get-team-size/${id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setTeamSize(result.data?.team || 0);
    } catch (error) { }
  }, [token]);

  const loadAssessmentCount = useCallback(async (id) => {
    try {
      const res = await axios.get(
        `${baseUrl}/api/v1/pms/manager/pms/status-count/${id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setAssessmentCount({
        completed: res.data.completedCount || 0,
        pending: res.data.pendingCount || 0,
      });
    } catch (err) {
      console.error("Error loading assessment count:", err);
    }
  }, [token]);

  useEffect(() => {
    if (managerId) {
      loadTeamSize(managerId);
      loadAssessmentCount(managerId);
    }
  }, [managerId, loadTeamSize, loadAssessmentCount]);


  useEffect(() => {
    if (!token) return;
    const socket = new SockJS(`${baseUrl}/ws?token=${token}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: async () => {
        client.subscribe("/user/queue/manager-notification", async (message) => {
          const newMsg = {
            title: "New Notification",
            message: message.body,
            timestamp: new Date().toISOString(),
          };
          setNotificationOpen(true);
          setNewAndUndelivered((prev) => [newMsg, ...prev]);
          setNotificationCount((prev) => prev + 1);
        });
      },
    });
    client.activate();
    return () => client.deactivate();
  }, [token, baseUrl]);

  const hasData =
    percentageData.completedPercentage !== 0 ||
    percentageData.pendingPercentage !== 0;

  const data = {
    labels: hasData ? ["Completed", "Pending"] : ["No Data"],
    datasets: [
      {
        data: hasData
          ? [
            percentageData.completedPercentage,
            percentageData.pendingPercentage,
          ]
          : [1],
        backgroundColor: hasData ? ["#4c4eafff", "#FF9800"] : ["#d3d3d3"],
        borderWidth: 1,
      },
    ],
  };

  const handleChartClick = (event) => {
    if (!hasData) return;
    const chart = chartRef.current;
    if (!chart) return;
    const elements = getElementsAtEvent(chart, event);
    if (elements.length > 0) {
      const clickedIndex = elements[0].index;
      if (clickedIndex === 0) navigate(`/completed-assessments/${managerId}`);
      else if (clickedIndex === 1)
        navigate(`/pending-assessments/${managerId}`);
    }
  };

  return (
    <div className="dashboard-container">
      {/* Sidebar */}
      <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
        <div className="profile-container">
          <img src={profile} alt="Profile" className="profile-pic" />
          <h2>{managerData?.name || "Manager"}</h2>
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
            <Link
              to={managerId ? `/my-team/${managerId}` : "#"}
              className={`sidebar-link-btn${!managerId ? " disabled" : ""}`}
            >
              My Team
            </Link>
          </li>
          <li>
            <Link to="/manager-profile">My Profile</Link>
          </li>
          <li className="mobile-only">
            <button
              className="logout button"
              onClick={() => {
                localStorage.clear();
                navigate("/");
              }}
            >
              Logout
            </button>
          </li>
        </ul>
      </div>

      {/* Main Content */}
      <div className="main-content">
        {/* Header */}
        <div className="dashboard-header">
          <button
            className="sidebar-toggle hum-btn"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            ☰
          </button>
          <h1>Manager PMS Dashboard</h1>

          <div className="header-icons" style={{ position: "relative" }}>
            <div style={{ position: "relative" }}>
              <Bell
                className="notification-icon"
                onClick={() => {
                  setNotificationOpen(!notificationOpen);
                  if (!notificationOpen) {
                    setNewAndUndelivered([]);
                    setNotificationCount(0);
                  }
                }}
              />
              {notificationCount > 0 && (
                <span className="notification-badge">{notificationCount}</span>
              )}
            </div>
            {notificationOpen && (
              <Notification
                notifications={
                  Array.isArray(notifications) ? notifications : []
                }
                onClose={() => setNotificationOpen(false)}
              />
            )}
            {/* Replaced Logout with Logo */}
            <img src={logo} alt="Company Logo" className="dashboard-logo" />
          </div>
        </div>

        {notificationOpen && (
          <Notification
            notifications={Array.isArray(notifications) ? notifications : []}
            onClose={() => setNotificationOpen(false)}
          />
        )}

        {/* Stats */}
        <div className="stats-container">
          <div className="stat-card">
            <Users className="stat-icon team-icon" />
            <Link
              to={managerId ? `/my-team/${managerId}` : "#"}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <div className="stat-text">
                <h2>Total Team Members</h2>
                <p>{teamSize}</p>
              </div>
            </Link>
          </div>

          <div className="stat-card">
            <NotebookPen className="stat-icon pending-icon" />
            <Link
              to={managerId ? `/pending-assessments/${managerId}` : "#"}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <div className="stat-text">
                <h2>Assessments Pending</h2>
                <p>{`${assessmentCount.pending}/${teamSize} (${teamSize !== 0
                  ? Math.round((assessmentCount.pending / teamSize) * 100)
                  : 0
                  }%)`}</p>
              </div>
            </Link>
          </div>

          <div className="stat-card">
            <CheckCircle className="stat-icon complete-icon" />
            <Link
              to={managerId ? `/completed-assessments/${managerId}` : "#"}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <div className="stat-text">
                <h2>Assessments Complete</h2>
                <p>{`${assessmentCount.completed}/${teamSize} (${teamSize !== 0
                  ? Math.round((assessmentCount.completed / teamSize) * 100)
                  : 0
                  }%)`}</p>
              </div>
            </Link>
          </div>
        </div>

        {/* Chart */}
        <div className="chart-container">
          <h3 className="chart-title">Assessment Status</h3>
          <div className="chart-wrapper">
            <Pie ref={chartRef} data={data} onClick={handleChartClick} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
