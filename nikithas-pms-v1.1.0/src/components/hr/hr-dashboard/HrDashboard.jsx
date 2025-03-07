import React from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bell } from 'lucide-react';
import './HrDashboard.css'; // Import external CSS
import logo from '../../../assets/images/nikithas-logo.png';
import profile from '../../../assets/images/profile1.jpg';

// Registering chart.js components
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
  // Bar chart data
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

  // Pie chart data
  const pieData = {
    labels: ['Completed', 'Pending'],
    datasets: [
      {
        data: [70, 30],
        backgroundColor: ['#28a745', '#ffa500'],
      },
    ],
  };

  return (
    <div className="hr-dashboard-container">
      <aside className="hr-dashboard-sidebar">
        <div className="hr-dashboard-profile-container">
          <img src={profile} alt="Profile" className="hr-dashboard-profileImg" />
          <h2>Avinash S. H.</h2>
        </div>
        <ul>
          <li><a href="#" className="active">HR Dashboard</a></li>
          <li><a href="#">Employee Details</a></li>
          <li><a href="#">Employee Performance</a></li>
          <li><a href="#">My Profile</a></li>
        </ul>
      </aside>

      <main className="hr-dashboard-main-content">
        <header className="hr-dashboard-header">
          <h1>HR PMS Dashboard</h1>
          <img src={logo} alt="Nikitha PMS" className="hr-dashboard-logo" />
          <div className="hr-dashboard-actions">
            <Bell className="hr-dashboard-notification-icon" size={24} />
            <button className="hr-dashboard-logoutButton">Logout</button>
          </div>
        </header>

        <section className="hr-dashboard-stats-container">
          <div className="hr-dashboard-stat-card"><h2>Total Employees</h2><p>1,254</p></div>
          <div className="hr-dashboard-stat-card"><h2>Completed Assessments</h2><p>876</p></div>
          <div className="hr-dashboard-stat-card"><h2>Pending Assessments</h2><p>375</p></div>
          <div className="hr-dashboard-stat-card"><h2>Completion Rate</h2><p>69.8%</p></div>
        </section>

        <section className="hr-dashboard-chart-container">
          <div className="hr-dashboard-chart-box hr-dashboard-bar-chart">
            <Bar data={barData} options={{ responsive: true, scales: { y: { beginAtZero: true } } }} />
          </div>
          <div className="hr-dashboard-chart-box hr-dashboard-pie-chart">
            <Pie data={pieData} options={{ responsive: true }} />
          </div>
        </section>
      </main>
    </div>
  );
};

export default HrDashboard;
