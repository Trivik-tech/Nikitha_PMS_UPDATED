// HrDashboard.jsx
import { React, useState, useEffect } from 'react';
import { FaUsers, FaClipboardCheck, FaExclamationTriangle, FaChartLine } from 'react-icons/fa';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bell } from 'lucide-react';
import { useNavigate, Link } from 'react-router-dom';
import './HrDashboard.css';
import './Responsive.css';

import logo from '../../../assets/images/nikithas-logo.png';
import profile from '../../../assets/images/profile1.jpg';
import Notification from "../../modal/notification/Notification";
import axios from 'axios';

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
  const [error, setError] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(window.innerWidth > 768);

  const isMobile = () => window.innerWidth <= 768;

  useEffect(() => {
    const handleResize = () => {
      setSidebarOpen(window.innerWidth  > 768);
    };
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    const loadTotalEmployees = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/pms/hr/total-employees");
        setTotalEmployees(response.data);
        setError(false);
      } catch (error) {
        setTotalEmployees({ totalEmployees: 0 });
        setError(true);
      }
    };
    loadTotalEmployees();
  }, []);

  const barData = {
    labels: ['IT', 'HR', 'Finance', 'Marketing', 'Sales'],
    datasets: [
      { label: 'Total Employees', data: [90, 60, 80, 70, 90], backgroundColor: '#007bff' },
      { label: 'Completed', data: [80, 55, 70, 60, 75], backgroundColor: '#28a745' },
      { label: 'Pending', data: [10, 5, 10, 10, 10], backgroundColor: '#ffc107' },
    ],
  };

  const pieData = {
    labels: ['Completed', 'Pending'],
    datasets: [{ data: [70, 30], backgroundColor: ['#28a745', '#ffa500'] }],
  };

  const pieOptions = {
    responsive: true,
    onClick: (event, elements) => {
      if (elements.length > 0) {
        const elementIndex = elements[0].index;
        if (elementIndex === 0) {
          navigate('/hr/completed-assessments');
        } else if (elementIndex === 1) {
          navigate('/hr/pending-assessments');
        }
      }
    },
  };

  const renderSidebarOverlay = () =>
    isMobile() && sidebarOpen ? (
      <div
        className="hr-dashboard-sidebar-overlay"
        onClick={() => setSidebarOpen(false)}
        aria-label="Close sidebar"
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100vw',
          height: '100vh',
          backgroundColor: 'rgba(0,0,0,0.5)',
          zIndex: 999
        }}
      />
    ) : null;

  return (
    <>
      <div className="hr-dashboard-container">
        {isMobile() && (
          <button
            className="hamburger"
            aria-label="Toggle sidebar"
            onClick={() => setSidebarOpen((prev) => !prev)}
          
          >
            ☰
          </button>
        )}
        <aside className={`hr-dashboard-sidebar${sidebarOpen ? ' open' : ' closed'}${isMobile() ? ' mobile' : ''}`}>
          <div className="hr-dashboard-profile-container">
            <img src={profile} alt="Profile" className="hr-dashboard-profileImg" />
            <h2 className="hr-dashboard-profile-name">Avinash S. H.</h2>
          </div>
          <ul>
            <li><Link to="/hr-dashboard" className="active">HR Dashboard</Link></li>
            <li><Link to="/employee-list">Employee List</Link></li>
            <li><Link to="/hr-startpms">Employee Performance</Link></li>
            <li><Link to="/hr-profile">My Profile</Link></li>
          </ul>
        </aside>
        {renderSidebarOverlay()}

        <main className="hr-dashboard-main-content">
         <header className="hr-dashboard-header fade-in-down">
  <div className="hr-dashboard-logo-container">
    <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
    <h1 className="hr-dashboard-title">HR PMS Dashboard</h1>
  </div>
  <div className="hr-dashboard-actions">
    <Bell className="notification-icon" onClick={() => setNotificationOpen(!notificationOpen)} />
  </div>
</header>


          <section className="hr-dashboard-stats-container fade-in-up">
            <div className="hr-dashboard-stat-card">
              <FaUsers className="stat-card-icon user" />
              <div>
                <h2>Total Employees</h2>
                <p>{totalEmployees?.totalEmployees}</p>
              </div>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaClipboardCheck className="stat-card-icon complete" />
              <div>
                <h2>Completed Assessments</h2>
                <p>876</p>
              </div>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaExclamationTriangle className="stat-card-icon pending" />
              <div>
                <h2>Pending Assessments</h2>
                <p>375</p>
              </div>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaChartLine className="stat-card-icon rate" />
              <div>
                <h2>Completion Rate</h2>
                <p>69.8%</p>
              </div>
            </div>
          </section>

          <section className="hr-dashboard-chart-container fade-in-up">
            <h2 className="hr-dashboard-assessment-heading">Assessment Status</h2>
            <div className="hr-dashboard-chart-box hr-dashboard-bar-chart">
              <Bar data={barData} options={{ responsive: true, scales: { y: { beginAtZero: true } } }} />
            </div>
            <div className="hr-dashboard-chart-box hr-dashboard-pie-chart">
              <Pie data={pieData} options={pieOptions} />
            </div>
          </section>
        </main>
      </div>

      {notificationOpen && (
        <div className="notification-modal-wrapper">
          <Notification onClose={() => setNotificationOpen(false)} />
        </div>
      )}
    </>
  );
};

export default HrDashboard;
