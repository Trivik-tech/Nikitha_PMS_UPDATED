// HrDashboard.jsx
import React, { useState, useEffect } from "react";
import {
  FaUsers,
  FaClipboardCheck,
  FaExclamationTriangle,
  FaChartLine,
} from "react-icons/fa";
import { Bar, Pie } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from "chart.js";
import { Bell } from "lucide-react";
import { useNavigate, Link } from "react-router-dom";
import "./HrDashboard.css";
import "./Responsive.css"; // ✅ included from one branch
import "../../urls/CommenUrl";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Notification from "../../modal/notification/Notification";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
);

const HrDashboard = () => {
  const navigate = useNavigate();
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [totalEmployees, setTotalEmployees] = useState(null);
  const [completionRate, setCompletionRate] = useState(0);
  const [pendingRate, setPendingRate] = useState(0);
  const [error, setError] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(window.innerWidth > 768);
  const [departments, setDepartments] = useState([]);
  const [employeeCount, setEmployeeCount] = useState([]);
  const [completed, setCompleted] = useState(0);
  const [pending, setPending] = useState(0);
  const [notifications, setNotifications] = useState([]);

  const isMobile = () => window.innerWidth <= 768;

  useEffect(() => {
    const handleResize = () => {
      setSidebarOpen(window.innerWidth > 768);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  useEffect(() => {
    const fetchTotalEmployees = async () => {
      try {
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/total-employees`);
        setTotalEmployees(response.data);
        setError(false);
      } catch (error) {
        setTotalEmployees({ totalEmployees: 0 });
        setError(true);
      }
    };
    fetchTotalEmployees();
  }, []);

  useEffect(() => {
    const fetchPercentageData = async () => {
      try {
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/percentage`);
        setCompletionRate(response.data.completedPercentage);
        setPendingRate(response.data.pendingPercentage);
        setError(false);
      } catch (error) {
        setCompletionRate(0);
        setPendingRate(0);
        setError(true);
      }
    };
    fetchPercentageData();
  }, []);

  useEffect(() => {
    loadAllData();
  }, []);

  const loadAllData = () => {
    getAllDepartment();
    getDepartmentsEmployeeCount();
    getKeyMatrix();
  };
 useEffect(() => {
  const jwtToken = localStorage.getItem("token");

  const socket = new SockJS(`http://localhost:8080/ws?token=${jwtToken}`);
  const client = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,

    onConnect: async () => {
      client.subscribe("/user/queue/hr-notification", async (message) => {
        const newMsg = {
          title: "New Notification",
          message: message.body,
          timestamp: new Date().toISOString(),
        };

        try {
          const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
            headers: { Authorization: `Bearer ${jwtToken}` },
          });

          const recent = res.data.map((msg) => ({
            title: "Recent Notification",
            message: msg.message || msg.content,
            timestamp: msg.timestamp,
          }));

          // First filter recent messages against newMsg
          const filtered = recent.filter((msg) => {
            const msgTime = Math.floor(new Date(msg.timestamp).getTime() / 1000);
            const newTime = Math.floor(new Date(newMsg.timestamp).getTime() / 1000);
            return !(msgTime === newTime && msg.message === newMsg.message);
          });

          const combined = [newMsg, ...filtered];

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
          console.error("Recent fetch error", err);
        }
      });

      // Initial fetch only once
      try {
        const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
          headers: { Authorization: `Bearer ${jwtToken}` },
        });

        const formatted = res.data.map((msg) => ({
          title: "Recent Notification",
          message: msg.message || msg.content,
          timestamp: msg.timestamp,
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
        console.error("Initial recent error", err);
      }
    },
  });

  client.activate();
  return () => client.deactivate();
}, []);

  const getKeyMatrix = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/keyMatrix`);
      setCompleted(result.data.completed);
      setPending(result.data.pending);
    } catch (error) {
      console.error(error);
    }
  };

  const getAllDepartment = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-departments`);
      setDepartments(result.data.departments);
    } catch (error) {
      console.error(error);
    }
  };

  const getDepartmentsEmployeeCount = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/employee-count-by-department`);
      setEmployeeCount(result.data.employees);
      console.log(result.data)
    } catch (error) {
      console.error(error);
    }
  };

  const barChartMinWidth = Math.max(480, departments.length * 120);

  const barData = {
    labels: departments,
    datasets: [
      {
        label: "Total Employees",
        data: employeeCount,
        backgroundColor: "#007bff",
        borderColor: "#0056b3",
        borderWidth: 1,
      },
      {
        label: "Completed",
        data: Array(departments.length).fill(Math.floor(Math.random() * 100)), // Placeholder
        backgroundColor: "#28a745",
        borderColor: "#1e7e34",
        borderWidth: 1,
      },
      {
        label: "Pending",
        data: Array(departments.length).fill(Math.floor(Math.random() * 50)), // Placeholder
        backgroundColor: "#ffc107",
        borderColor: "#e0a800",
        borderWidth: 1,
      },
    ],
  };

  const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: "top", labels: { padding: 20, font: { size: 12 } } },
      title: {
        display: true,
        text: "Department-wise Employee Statistics",
        font: { size: 16, weight: "bold" },
        padding: { top: 10, bottom: 30 },
      },
    },
    scales: {
      x: {
        beginAtZero: true,
        ticks: {
          maxRotation: 45,
          minRotation: 0,
          font: { size: 11 },
          autoSkip: false, // ✅ retain the version that avoids label cut-offs
        },
        grid: { display: false },
      },
      y: {
        beginAtZero: true,
        ticks: { font: { size: 11 } },
        grid: { color: "#e0e0e0" },
      },
    },
    interaction: { intersect: false, mode: "index" },
    animation: { duration: 1000, easing: "easeOutQuart" },
  };

  const pieData = {
    labels: ["Completed", "Pending"],
    datasets: [
      {
        data: [completionRate, pendingRate],
        backgroundColor: ["#28a745", "#ffa500"],
        borderColor: ["#1e7e34", "#e0a800"],
        borderWidth: 2,
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom",
        labels: { padding: 20, font: { size: 12 } },
      },
      title: {
        display: true,
        text: "Overall Assessment Status",
        font: { size: 16, weight: "bold" },
        padding: { top: 10, bottom: 20 },
      },
    },
    onClick: (event, elements) => {
      if (elements.length > 0) {
        const index = elements[0].index;
        if (index === 0) navigate("/hr/completed-assessments");
        else if (index === 1) navigate("/hr/pending-assessments");
      }
    },
    animation: { duration: 1000, easing: "easeOutQuart" },
  };

  const renderSidebarOverlay = () =>
    isMobile() && sidebarOpen ? (
      <div
        className="hr-dashboard-sidebar-overlay"
        onClick={() => setSidebarOpen(false)}
        aria-label="Close sidebar"
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100vw",
          height: "100vh",
          backgroundColor: "rgba(0,0,0,0.5)",
          zIndex: 1,
        }}
      />
    ) : null;

  return (
    <>
      <div className="hr-dashboard-container">
        <aside
          className={`hr-dashboard-sidebar${sidebarOpen ? " open" : " closed"}${isMobile() ? " mobile" : ""}`}
          style={
            isMobile()
              ? {
                  position: "fixed",
                  top: 0,
                  left: 0,
                  height: "100vh",
                  zIndex: 999,
                }
              : {}
          }
        >
          {isMobile() && sidebarOpen && (
            <button
              className="hamburger sidebar-hamburger"
              aria-label="Close sidebar"
              onClick={() => setSidebarOpen(false)}
              style={{ position: "absolute", top: 5, right: 150 }}
            >
              ☰
            </button>
          )}
          <div className="hr-dashboard-profile-container">
            <img src={profile} alt="Profile" className="hr-dashboard-profileImg" />
            <h2 className="hr-dashboard-profile-name">Avinash S. H.</h2>
          </div>
          <ul>
            <li><Link to="/hr-dashboard" className="active">HR Dashboard</Link></li>
            <li><Link to="/employee-list">Employee List</Link></li>
            <li><Link to="/hr-startpms">Employee Performance</Link></li>
            <li><Link to="/hr-profile">My Profile</Link></li>
            {isMobile() && (
              <li>
                <button onClick={() => { localStorage.clear(); navigate("/"); }} className="logout-button">
                  Logout
                </button>
              </li>
            )}
          </ul>
        </aside>
        {renderSidebarOverlay()}

        <main className="hr-dashboard-main-content">
          <header className="hr-dashboard-header fade-in-down">
            {isMobile() && !sidebarOpen && (
              <button
                className="hamburger"
                aria-label="Open sidebar"
                onClick={() => setSidebarOpen(true)}
                style={{ top: -7, left: -30 }}
              >
                ☰
              </button>
            )}
            <div className="hr-dashboard-logo-container">
              <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
              <h1 className="hr-dashboard-title">HR PMS Dashboard</h1>
            </div>
            <div className="hr-dashboard-actions">
              <Bell className=".hr-dashboard-notificationButton" onClick={() => setNotificationOpen(!notificationOpen)} />
              <button
                className="hr-dashboard-logoutButton desktop-only"
                onClick={() => {
                  localStorage.clear();
                  navigate("/");
                }}
              >
                Logout
              </button>
            </div>
          </header>

          <section className="hr-dashboard-stats-container fade-in-up">
            <div className="hr-dashboard-stat-card"><FaUsers className="stat-card-icon user" /><h2>Total Employees</h2><p>{totalEmployees?.totalEmployees ?? "-"}</p></div>
            <div className="hr-dashboard-stat-card"><FaClipboardCheck className="stat-card-icon complete" /><h2>Completed Assessments</h2><p>{completed}</p></div>
            <div className="hr-dashboard-stat-card"><FaExclamationTriangle className="stat-card-icon pending" /><h2>Pending Assessments</h2><p>{pending}</p></div>
            <div className="hr-dashboard-stat-card"><FaChartLine className="stat-card-icon rate" /><h2>Completion Rate</h2><p>{completionRate}%</p></div>
          </section>

          <section className="hr-dashboard-chart-container fade-in-up">
            <h2 className="hr-dashboard-assessment-heading">Assessment Status</h2>
            <div className="hr-dashboard-charts-wrapper">
              <div className="hr-dashboard-chart-box hr-dashboard-bar-chart">
                <div className="chart-scroll-container" style={isMobile() ? { width: "100%", overflowX: "auto" } : {}}>
                  <div style={isMobile() ? { minWidth: `${barChartMinWidth}px`, width: `${barChartMinWidth}px`, height: "320px" } : { width: "100%", height: "320px" }}>
                    <Bar data={barData} options={barOptions} />
                  </div>
                </div>
              </div>
              <div className="hr-dashboard-chart-box hr-dashboard-pie-chart">
                <Pie data={pieData} options={pieOptions} />
              </div>
            </div>
          </section>
        </main>
      </div>

      {notificationOpen && (
        <div className="notification-modal-wrapper">
         <Notification
  onClose={() => setNotificationOpen(false)}
  notifications={notifications}
/>
        </div>
      )}
    </>
  );
};

export default HrDashboard;