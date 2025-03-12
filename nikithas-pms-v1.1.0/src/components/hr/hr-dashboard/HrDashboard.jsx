import { React, useState, useEffect } from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bell } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import './HrDashboard.css';

import logo from '../../../assets/images/nikithas-logo.png';
import profile from '../../../assets/images/profile1.jpg';
import Notification from "../../modal/notification/Notification";

// Register chart.js components
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
  const [animate, setAnimate] = useState(false);

  useEffect(() => {
    setTimeout(() => setAnimate(true), 200);
  }, []);

  const barData = {
    labels: ['IT', 'HR', 'Finance', 'Marketing', 'Sales'],
    datasets: [
      {
        label: 'Total Employees',
        data: [90, 60, 80, 70, 90],
        backgroundColor: '#007bff',
      },
      {
        label: 'Completed',
        data: [80, 55, 70, 60, 75],
        backgroundColor: '#28a745',
      },
      {
        label: 'Pending',
        data: [10, 5, 10, 10, 10],
        backgroundColor: '#ffc107',
      },
    ],
  };

  const pieData = {
    labels: ['Completed', 'Pending'],
    datasets: [
      {
        data: [70, 30],
        backgroundColor: ['#28a745', '#ffa500'],
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    onClick: (event, elements) => {
      if (elements.length > 0) {
        const elementIndex = elements[0].index;
        if (elementIndex === 1) { // Clicking on 'Pending'
          navigate('/hr/pending-assessments');
        }
      }
    }
  };

  return (
    <>
      <div className="hr-dashboard-container">
        <aside className="hr-dashboard-sidebar slide-in">
          <div className="hr-dashboard-profile-container">
            <img src={profile} alt="Profile" className="hr-dashboard-profileImg" />
            <h2 className="hr-dashboard-profile-name">Avinash S. H.</h2>
          </div>
          <ul>
            <li><Link to="/hr-dashboard" className="active">HR Dashboard</Link></li>
            <li><Link to="/employee-details">Employee Details</Link></li>
            <li><Link to="/hr-startpms">Employee Performance</Link></li>
            <li><Link to="/hr-profile">My Profile</Link></li>
          </ul>
        </aside>

        <main className="hr-dashboard-main-content">
          <header className="hr-dashboard-header fade-in-down">
            <h1>HR PMS Dashboard</h1>
            <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
            <div className="hr-dashboard-actions">
              <Bell className="notification-icon" onClick={() => setNotificationOpen(!notificationOpen)} />
              <Link to="/" className="hr-dashboard-logoutButton">Logout</Link>
            </div>
          </header>

          <section className="hr-dashboard-stats-container fade-in-up">
            <div className="hr-dashboard-stat-card">
              <h2>Total Employees</h2>
              <p>1,254</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <h2>Completed Assessments</h2>
              <p>876</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <h2>Pending Assessments</h2>
              <p>375</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <h2>Completion Rate</h2>
              <p>69.8%</p>
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