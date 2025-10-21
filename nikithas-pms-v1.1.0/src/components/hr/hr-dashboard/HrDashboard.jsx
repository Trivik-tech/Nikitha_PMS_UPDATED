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
  const [notificationCount, setNotificationCount] = useState(0);
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
  const [hrId, setHrId] = useState(null);
  const [completedByDept, setCompletedByDept] = useState({});
  const [pendingByDept, setPendingByDept] = useState({});
  const [hrprofile, setProfile] = useState(null);
  const [filterType, setFilterType] = useState(""); // default empty for "Filter"

  const token = localStorage.getItem("token");
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
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/total-employees`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTotalEmployees(response.data);
      } catch {
        setTotalEmployees({ totalEmployees: 0 });
        setError(true);
      }
    };
    fetchTotalEmployees();
  }, [token]);

  useEffect(() => {
    const fetchPercentageData = async () => {
      try {
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/percentage`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setCompletionRate(response.data.completedPercentage);
        setPendingRate(response.data.pendingPercentage);
      } catch {
        setCompletionRate(0);
        setPendingRate(0);
        setError(true);
      }
    };
    fetchPercentageData();
  }, [token]);

  useEffect(() => {
    loadAllData();
  }, [filterType]);

  const loadAllData = async () => {
    await getAllDepartment();
    await getDepartmentsEmployeeCount();
    await getKeyMatrix();
    await loadHr();
    await loadCompletedAndPendingByDepartment();
  };

  const getKeyMatrix = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/status-count`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCompleted(result.data.completedCount);
      setPending(result.data.pendingCount);
    } catch (error) {
      console.error("Status Count Error:", error);
    }
  };

  const getAllDepartment = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-departments`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDepartments(result.data.departments);
    } catch (error) {
      console.error(error);
    }
  };

  const getDepartmentsEmployeeCount = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/employee-count-by-department?filter=${filterType}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setEmployeeCount(result.data.employees);
    } catch (error) {
      console.error(error);
    }
  };

  const loadHr = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/profile`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setHrId(result.data.profile.hrId);
      setProfile(result.data.profile);
    } catch (error) {
      console.log(error.message);
    }
  };

  const loadCompletedAndPendingByDepartment = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-completed-pending-department?filter=${filterType}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data = result.data;
      const completed = {};
      const pending = {};

      Object.keys(data).forEach((dept) => {
        completed[dept] = data[dept].completed || 0;
        pending[dept] = data[dept].pending || 0;
      });

      setCompletedByDept(completed);
      setPendingByDept(pending);
    } catch (error) {
      console.log("Error loading dept KRA/KPI stats:", error.message);
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
        data: departments.map((dept) => completedByDept[dept] || 0),
        backgroundColor: "#28a745",
        borderColor: "#1e7e34",
        borderWidth: 1,
      },
      {
        label: "Pending",
        data: departments.map((dept) => pendingByDept[dept] || 0),
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
      legend: { position: "top" },
      title: {
        display: true,
        text: `Department-wise Employee Statistics${filterType ? ` (${filterType})` : ""}`,
        font: { size: 16, weight: "bold" },
      },
    },
    scales: {
      x: { beginAtZero: true, ticks: { font: { size: 11 } } },
      y: { beginAtZero: true, ticks: { font: { size: 11 } } },
    },
  };

  const hasPieData = completionRate !== 0 || pendingRate !== 0;

  const pieData = {
    labels: hasPieData ? ["Completed", "Pending"] : ["No Data"],
    datasets: [
      {
        data: hasPieData ? [completionRate, pendingRate] : [1],
        backgroundColor: hasPieData ? ["#28a745", "#ffa500"] : ["#d3d3d3"],
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { position: "bottom" } },
  };

  return (
    <>
      <div className="hr-dashboard-container">
        {/* Sidebar */}
        <aside
          className={`hr-dashboard-sidebar${sidebarOpen ? " open" : " closed"}${isMobile() ? " mobile" : ""}`}
        >
          <div className="hr-dashboard-profile-container">
            <img src={profile} alt="Profile" className="hr-dashboard-profileImg" />
            <h2 className="hr-dashboard-profile-name">
              {hrprofile?.name || "HR User"}
            </h2>
          </div>
          <ul>
            <li><Link to="/hr-dashboard" className="active">HR Dashboard</Link></li>
            <li><Link to="/employee-list">Employee List</Link></li>
            <li><Link to="/hr-startpms">Employee Performance</Link></li>
            <li><Link to="/hr-profile">My Profile</Link></li>
            <button
              className="hr-dashboard-logoutButton desktop-only"
              onClick={() => {
                localStorage.clear();
                navigate("/");
              }}
            >
              Logout
            </button>
          </ul>
        </aside>

        <main className="hr-dashboard-main-content">
          <header className="hr-dashboard-header fade-in-down">
            <div className="hr-dashboard-logo-container">
              <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
              <h1 className="hr-dashboard-title">HR PMS Dashboard</h1>
            </div>
            <div className="hr-dashboard-actions">
              <div className="notification-wrapper">
                <Bell
                  className="hr-dashboard-notificationButton"
                  onClick={() => {
                    setNotificationOpen(!notificationOpen);
                    if (!notificationOpen) setNotificationCount(0);
                  }}
                />
                {notificationCount > 0 && (
                  <span className="notification-badge">{notificationCount}</span>
                )}
              </div>
            </div>
          </header>

          <section className="hr-dashboard-stats-container fade-in-up">
            <div className="hr-dashboard-stat-card"><FaUsers className="stat-card-icon user" /><h2>Total Employees</h2><p>{totalEmployees?.totalEmployees ?? "-"}</p></div>
            <div className="hr-dashboard-stat-card"><FaClipboardCheck className="stat-card-icon complete" /><h2>Completed Assessments</h2><p>{completed}</p></div>
            <div className="hr-dashboard-stat-card"><FaExclamationTriangle className="stat-card-icon pending" /><h2>Pending Assessments</h2><p>{pending}</p></div>
            <div className="hr-dashboard-stat-card"><FaChartLine className="stat-card-icon rate" /><h2>Completion Rate</h2><p>{completionRate}%</p></div>
          </section>

          {/* Chart Section */}
          <section className="hr-dashboard-chart-container fade-in-up">
            <h2 className="hr-dashboard-assessment-heading">Assessment Status</h2>

            <div className="hr-dashboard-charts-wrapper">
              {/* Bar Chart */}
              <div className="hr-dashboard-chart-box hr-dashboard-bar-chart">
                {/* Filter Dropdown inside Bar Chart, top-left */}
                <div className="chart-filter-inside">
                  <select
                    className="chart-filter-dropdown"
                    value={filterType}
                    onChange={(e) => setFilterType(e.target.value)}
                  >
                    <option value="" disabled>Filter</option>
                    <option value="Monthly">Monthly</option>
                    <option value="Quarterly">Quarterly</option>
                    <option value="Yearly">Yearly</option>
                  </select>
                </div>

                <div className="chart-scroll-container">
                  <div style={{ minWidth: `${barChartMinWidth}px`, height: "320px" }}>
                    <Bar data={barData} options={barOptions} />
                  </div>
                </div>
              </div>

              {/* Pie Chart */}
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
