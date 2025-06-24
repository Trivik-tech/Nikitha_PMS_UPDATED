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
import "./Responsive.css";
import "../../urls/CommenUrl";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Notification from "../../modal/notification/Notification";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const HrDashboard = () => {
  const navigate = useNavigate();
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [allMessages, setAllMessages] = useState([]);
  const [newAndUndelivered, setNewAndUndelivered] = useState([]);
  const [totalEmployees, setTotalEmployees] = useState(null);
  const [completionRate, setCompletionRate] = useState(0);
  const [pendingRate, setPendingRate] = useState(0);
  const [error, setError] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(window.innerWidth > 768);
  const [departments, setDepartments] = useState([]);
  const [employeeCount, setEmployeeCount] = useState([]);
  const [completed, setCompleted] = useState(0);
  const [pending, setPending] = useState(0);

  const jwtToken = 'PASTE_YOUR_HR_JWT_TOKEN_HERE';

  useEffect(() => {
    const socket = new SockJS(`http://localhost:8080/ws?token=${jwtToken}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: async () => {
        client.subscribe('/user/queue/hr-notification', async (message) => {
          const newMsg = {
            content: message.body,
            timestamp: new Date().toISOString(),
            delivered: true,
          };
          const updatedNew = [newMsg, ...newAndUndelivered];
          setNewAndUndelivered(updatedNew);

          try {
            const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
              headers: { Authorization: `Bearer ${jwtToken}` }
            });

            const recent = res.data;
            const filteredRecent = recent.filter(msg => {
              const msgTime = Math.floor(new Date(msg.timestamp).getTime() / 1000);
              const newMsgTime = Math.floor(new Date(newMsg.timestamp).getTime() / 1000);
              return !(msgTime === newMsgTime && msg.content === newMsg.content);
            });

            const combined = [...updatedNew, ...filteredRecent];
            const unique = Array.from(new Map(
              combined.map(msg => [
                `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.content}`,
                msg
              ])
            ).values());

            setAllMessages(unique.slice(0, 50));
          } catch (err) {
            console.error('❌ Error fetching recent:', err);
          }
        });

        try {
          const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
            headers: { Authorization: `Bearer ${jwtToken}` }
          });

          const messages = res.data;
          const undelivered = messages.filter(m => !m.delivered);
          const recent = messages.filter(m => m.delivered);

          setNewAndUndelivered(undelivered);
          const combined = [...undelivered, ...recent];

          const unique = Array.from(new Map(
            combined.map(msg => [
              `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.content}`,
              msg
            ])
          ).values());

          setAllMessages(unique.slice(0, 50));
        } catch (err) {
          console.error('❌ Initial fetch error:', err);
        }
      }
    });

    client.activate();
    return () => client.deactivate();
  }, []);
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
        data: Array(departments.length).fill(Math.floor(Math.random() * 100)),
        backgroundColor: "#28a745",
        borderColor: "#1e7e34",
        borderWidth: 1,
      },
      {
        label: "Pending",
        data: Array(departments.length).fill(Math.floor(Math.random() * 50)),
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
        ticks: { maxRotation: 45, minRotation: 0, font: { size: 11 }, autoSkip: false },
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
      legend: { position: "bottom", labels: { padding: 20, font: { size: 12 } } },
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

  return (
    <div className="hr-dashboard-container">
      <header className="hr-dashboard-header">
        <div className="hr-dashboard-logo-container">
          <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
          <h1 className="hr-dashboard-title">HR PMS Dashboard</h1>
        </div>
        <div className="hr-dashboard-actions">
          <Bell
            className="hr-dashboard-notificationButton"
            onClick={() => {
              setNotificationOpen(!notificationOpen);
              if (!notificationOpen) setNewAndUndelivered([]);
            }}
          />
          {newAndUndelivered.length > 0 && (
            <span className="notif-count">{newAndUndelivered.length}</span>
          )}
        </div>
      </header>

      {/* Charts and stats */}
      <section className="hr-dashboard-stats-container">
        <div className="hr-dashboard-stat-card"><FaUsers /> Total Employees: {totalEmployees?.totalEmployees}</div>
        <div className="hr-dashboard-stat-card"><FaClipboardCheck /> Completed: {completed}</div>
        <div className="hr-dashboard-stat-card"><FaExclamationTriangle /> Pending: {pending}</div>
        <div className="hr-dashboard-stat-card"><FaChartLine /> Completion Rate: {completionRate}%</div>
      </section>

      <section className="hr-dashboard-chart-container">
        <div className="bar-chart">
          <Bar data={barData} options={barOptions} />
        </div>
        <div className="pie-chart">
          <Pie data={pieData} options={pieOptions} />
        </div>
      </section>

      {notificationOpen && (
        <Notification onClose={() => setNotificationOpen(false)} allMessages={allMessages} />
      )}
    </div>
  );
};

export default HrDashboard;