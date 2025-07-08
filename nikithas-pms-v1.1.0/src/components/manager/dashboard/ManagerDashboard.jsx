import React, { useState, useEffect, useRef } from "react";
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
import { baseUrl } from '../../urls/CommenUrl'

Chart.register(ArcElement, Tooltip, Legend);

const ManagerDashboard = () => {
  const navigate = useNavigate();
  const chartRef = useRef();

  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0);
  const [managerData, setManagerData] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [teamSize, setTeamSize] = useState(0);
  const [percentageData, setPercentageData] = useState({
    completedPercentage: 0,
    pendingPercentage: 0,
  });
  const [managerId, setManagerId] = useState("");

  const token = localStorage.getItem("token");

  // Logout if token is missing
  useEffect(() => {
    if (!token) {
      localStorage.clear();
      navigate("/");
    }
  }, [token, navigate]);

  // Load profile
  useEffect(() => {
    const getProfile = async () => {
      try {
        const res = await axios.get(`${baseUrl}/api/v1/pms/manager/profile`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const id = res.data.managerId;
        setManagerId(id);
        setManagerData(res.data);
        await getPercentage(id);
      } catch (error) {
        console.error("❌ Error fetching profile:", error);
        if (error.response?.status === 401) {
          localStorage.clear();
          navigate("/");
        }
      }
    };
    if (token) getProfile();
  }, [token, navigate]);

  useEffect(() => {
    if (managerId) loadTeamSize(managerId);
  }, [managerId]);

  const getPercentage = async (id) => {
    try {
      const res = await axios.get(`${baseUrl}/api/v1/pms/manager/percentage/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPercentageData(res.data);
    } catch (error) {
      console.error("❌ Error fetching percentage:", error);
    }
  };

  const loadTeamSize = async (id) => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/manager/get-team-size/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setTeamSize(result.data?.team || 0);
    } catch (error) {
      console.error("❌ Error fetching team size:", error.message);
    }
  };

  // WebSocket Notifications
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

          const updated = [newMsg, ...notifications];
          setNotificationCount((prev) => prev + 1);

          try {
            const res = await fetch(`${baseUrl}/api/v1/pms/recent`, {
              headers: { Authorization: `Bearer ${token}` },
            });
            const recent = await res.json();
            const formatted = recent.map((msg) => ({
              title: "Recent Notification",
              message: msg.message || msg.content || "No content",
              timestamp: msg.timestamp || new Date().toISOString(),
            }));

            const filteredRecent = formatted.filter((msg) => {
              const msgTime = Math.floor(new Date(msg.timestamp).getTime() / 1000);
              const newMsgTime = Math.floor(new Date(newMsg.timestamp).getTime() / 1000);
              return !(msgTime === newMsgTime && msg.message === newMsg.message);
            });

            const combined = [...updated, ...filteredRecent];
            const unique = Array.from(
              new Map(
                combined.map((msg) => [
                  `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.message}`,
                  msg,
                ])
              ).values()
            );

            setNotifications(unique.slice(0, 50));
            setNotificationOpen(true);
          } catch (err) {
            console.error("❌ Error fetching recent:", err);
          }
        });

        try {
          const res = await fetch(`${baseUrl}/api/v1/pms/recent`, {
            headers: { Authorization: `Bearer ${token}` },
          });

          const messages = await res.json();
          const formatted = messages.map((msg) => ({
            title: "Recent Notification",
            message: msg.message || msg.content || "No content",
            timestamp: msg.timestamp || new Date().toISOString(),
          }));

          const unique = Array.from(
            new Map(
              formatted.map((msg) => [
                `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.message}`,
                msg,
              ])
            ).values()
          );

          setNotifications(unique.slice(0, 50));
        } catch (err) {
          console.error("❌ Initial fetch error:", err);
        }
      },
      onStompError: (frame) => {
        console.error("🔴 STOMP error:", frame);
      },
    });

    client.activate();
    return () => client.deactivate();
  }, [token, notifications]);

  const hasData = percentageData.completedPercentage !== 0 || percentageData.pendingPercentage !== 0;

  const data = {
    labels: hasData ? ["Completed", "Pending"] : ["No Data"],
    datasets: [
      {
        data: hasData
          ? [percentageData.completedPercentage, percentageData.pendingPercentage]
          : [1],
        backgroundColor: hasData ? ["#4CAF50", "#FF9800"] : ["#d3d3d3"],
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
      else if (clickedIndex === 1) navigate(`/pending-assessments/${managerId}`);
    }
  };

  return (
    <div className="dashboard-container">
      <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
        <div className="profile-container">
          <img src={profile} alt="Profile" className="profile-pic" />
          <h2>{managerData?.name || "Manager"}</h2>
        </div>
        <ul>
          <li>
            <NavLink to="/manager-dashboard" className={({ isActive }) => (isActive ? "active" : "")}>
              Dashboard
            </NavLink>
          </li>
          <li>
            <Link
              to={managerId ? `/my-team/${managerId}` : "#"}
              className={`sidebar-link-btn${!managerId ? " disabled" : ""}`}
              style={{
                pointerEvents: managerId ? "auto" : "none",
                opacity: managerId ? 1 : 0.5,
                textDecoration: "none",
                color: "inherit"
              }}
            >
              My Team
            </Link>
          </li>
          <li>
            <Link to="/manager-profile">My Profile</Link>
          </li>
          <li>
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

      <div className="main-content">
        <div className="dashboard-header">
          <button className="sidebar-toggle hum-btn" onClick={() => setSidebarOpen(!sidebarOpen)}>☰</button>
          <h1>Manager PMS Dashboard</h1>
          <img src={logo} alt="Logo" className="dashboard-logo" />
          <div className="header-icons" style={{ position: "relative" }}>
            <div style={{ position: "relative" }}>
              <Bell
                className="notification-icon"
                onClick={() => {
                  setNotificationOpen(!notificationOpen);
                  if (!notificationOpen) setNotificationCount(0);
                }}
              />
              {notificationCount > 0 && (
                <span className="notification-badge">{notificationCount}</span>
              )}
            </div>
            {notificationOpen && (
              <Notification
                notifications={Array.isArray(notifications) ? notifications : []}
                onClose={() => setNotificationOpen(false)}
              />
            )}
            <button
              className="logout-btn desktop-only"
              onClick={() => {
                localStorage.clear();
                navigate("/");
              }}
            >
              Logout
            </button>
          </div>
        </div>

        <div className="stats-container">
          <div className="stat-card">
            <Users className="stat-icon team-icon" />
            <div className="stat-text">
              <h2>Total Team Members</h2>
              <p>{teamSize}</p>
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
            <Pie ref={chartRef} data={data} onClick={handleChartClick} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
