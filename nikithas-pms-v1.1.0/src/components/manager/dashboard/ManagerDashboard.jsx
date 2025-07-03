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

Chart.register(ArcElement, Tooltip, Legend);

const ManagerDashboard = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0);
  const [managerData, setManagerData] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [percentageData, setPercentageData] = useState({
    completedPercentage: 0,
    pendingPercentage: 0,
  });
  const [managerId, setManagerId] = useState("");
  const token = localStorage.getItem("token");

  const chartRef = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    const getProfile = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/v1/pms/manager/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const id = res.data.managerId;
        setManagerId(id);
        setManagerData(res.data);
        await getpercentage(id); // ✅ Call getpercentage with the actual ID
      } catch (error) {
        console.error("❌ Error fetching profile:", error);
      }
    };

    getProfile();
  }, []);

  const getpercentage = async (id) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/v1/pms/manager/percentage/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPercentageData(res.data);
      console.log("✅ PMS Percentage:", res.data);
    } catch (error) {
      console.error("❌ Error fetching percentage:", error);
    }
  };

  useEffect(() => {
    const socket = new SockJS(`http://localhost:8080/ws?token=${token}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: async () => {
        console.log("✅ WebSocket connected");

        client.subscribe("/user/queue/manager-notification", async (message) => {
          const newMsg = {
            title: "New Notification",
            message: message.body,
            timestamp: new Date().toISOString(),
          };

          const updated = [newMsg, ...notifications];
          setNotificationCount((prev) => prev + 1);

          try {
            const res = await fetch("http://localhost:8080/api/v1/pms/recent", {
              headers: { Authorization: `Bearer ${token}` },
            });
            const recent = await res.json();
            const formattedRecent = recent.map((msg) => ({
              title: "Recent Notification",
              message: msg.message || msg.content || "No content",
              timestamp: msg.timestamp || new Date().toISOString(),
            }));

            const filteredRecent = formattedRecent.filter((msg) => {
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
          const res = await fetch("http://localhost:8080/api/v1/pms/recent", {
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
  }, [token]);

 const hasData = percentageData.completedPercentage !== 0 || percentageData.pendingPercentage !== 0;

const data = {
  labels: hasData ? ["Completed", "Pending"] : ["No Data"],
  datasets: [
    {
      data: hasData
        ? [percentageData.completedPercentage, percentageData.pendingPercentage]
        : [1],
      backgroundColor: hasData
        ? ["#4CAF50", "#FF9800"]
        : ["#d3d3d3"], // gray for no data
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
          <h2>
            {managerData?.firstName || "Ravindra"} {managerData?.lastName || "Kulkarni"}
          </h2>
        </div>
        <ul>
          <li>
            <NavLink to="/manager-dashboard" className={({ isActive }) => (isActive ? "active" : "")}>
              Dashboard
            </NavLink>
          </li>
          <li>
            <Link to={`/my-team/${managerId}`}>My Team</Link>
          </li>
          <li>
            <Link to="/manager-profile">My Profile</Link>
          </li>
          <li>
            <Link to="/" className="logout button">Logout</Link>
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
        if (!notificationOpen) setNotificationCount(0); // ✅ Reset count on open
      }}
    />
    {notificationCount > 0 && (
      <span className="notification-badge">{notificationCount}</span> // ✅ Show badge
    )}
  </div>
            {notificationOpen && (
              <Notification
                notifications={Array.isArray(notifications) ? notifications : []}
                onClose={() => setNotificationOpen(false)}
              />
            )}
            <Link to="/" className="logout-btn desktop-only">Logout</Link>
          </div>
        </div>

        <div className="stats-container">
          <div className="stat-card">
            <Users className="stat-icon team-icon" />
            <div className="stat-text">
              <h2>Total Team Members</h2>
              <p>2</p>
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
