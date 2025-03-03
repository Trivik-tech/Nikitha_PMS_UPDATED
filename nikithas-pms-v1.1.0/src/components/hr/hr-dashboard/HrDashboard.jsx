import React from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import './HrDashboard.css'; // Import the external CSS file

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

const Dashboard = () => {
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
        backgroundColor: ['#28a745', '#ffc107'],
      },
    ],
  };

  return (
    <div className="container">
      <div className="sidebar">
        <img src="profile.png" alt="Profile" className="profileImg" />
        <h1 className="sidebarTitle">Hi!<br />Avinash S H</h1>
        <div className="menu">
          <a href="#" className="activeMenuLink">HR Dashboard</a>
          <a href="#">Employee Details</a>
          <a href="#">Employee Performance</a>
          <a href="#">My Profile</a>
        </div>
      </div>
      <div className="content">
        <div className="header">
          <img src="logo.png" alt="Nikitha PMS" className="logo" />
          <button className="logoutButton">Logout</button>
        </div>
        {/* <h1>Employee Assessment Status</h1> */}
        <div className="statusCards">
          <div className="statusCard">
            <h5>Total Employees</h5>
            <div className="count">1,254</div>
          </div>
          <div className="statusCard">
            <h5>Completed Assessments</h5>
            <div className="count">876</div>
          </div>
          <div className="statusCard">
            <h5>Pending Assessments</h5>
            <div className="count">375</div>
          </div>
          <div className="statusCard">
            <h5>Completion Rate</h5>
            <div className="count">69.8%</div>
          </div>
        </div>
        <div className="charts">
          <div className="chart">
            
            <Bar data={barData} options={{ responsive: true, scales: { y: { beginAtZero: true } } }} />
          </div>
          <div className="chart">
           
            <Pie data={pieData} options={{ responsive: true }} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
