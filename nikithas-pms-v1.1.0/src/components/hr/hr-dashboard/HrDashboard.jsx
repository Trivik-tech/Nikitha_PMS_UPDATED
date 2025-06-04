import { React, useState, useEffect } from 'react';
import { FaUsers, FaClipboardCheck, FaExclamationTriangle, FaChartLine } from 'react-icons/fa';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Bell } from 'lucide-react';
import { useNavigate, Link } from 'react-router-dom';
import './HrDashboard.css';
import '../../urls/CommenUrl'

import logo from '../../../assets/images/nikithas-logo.png';
import profile from '../../../assets/images/profile1.jpg';
import Notification from "../../modal/notification/Notification";
import axios from 'axios';
import { baseUrl } from '../../urls/CommenUrl';

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
  const [totalEmployees, setTotalEmployees] = useState(null);
  const [error, setError] = useState(false);
  const [departments,setDepartments]=useState([]);
  const[employeeCount,setEmployeeCount]=useState([])
  const[completed,setCompleted]=useState();
  const[pending,setPending]=useState();

  useEffect(() => {
    setTimeout(() => setAnimate(true), 200);
  }, []);

  useEffect(() => {
    const loadTotalEmployees = async () => {
      try {
        
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/total-employees`);
        setTotalEmployees(response.data);
        setError(false);
      } catch (error) {
        console.error("Error fetching total employees:", error.message);
        setTotalEmployees({ totalEmployees: 0 });
        setError(true);
      }
    };

    loadTotalEmployees();
    getAllDepartment();
    getDepartmentsEmployeeCount()
    getKeyMatrix()
  }, []);

  const getKeyMatrix=async()=>{
    try{

      const result=await axios.get(`${baseUrl}/api/v1/pms/hr/keyMatrix`)
      console.log(result.data)
      setCompleted(result.data.completed)
      setPending(result.data.pending)

    }catch(error){
      console.error(error)

    }
  }

  const getAllDepartment=async()=>{
    try{
      const result=await axios.get(`${baseUrl}/api/v1/pms/hr/get-departments`);

      console.log(result.data)
      setDepartments(result.data.departments)


    }catch(error){
      console.error(error)
    }
  }

  const getDepartmentsEmployeeCount=async()=>{
    try{

      const result=await axios.get(`${baseUrl}/api/v1/pms/hr/employee-count-by-department`)
      console.log(result.data)
      setEmployeeCount(result.data.employees)
    }catch(error){
      console.error(error)
    }
  }

  const barData = {
    labels: departments,
    datasets: [
      {
        label: 'Total Employees',
        data: employeeCount,
        backgroundColor: '#007bff',
        borderColor: '#0056b3',
        borderWidth: 1,
      },
      {
        label: 'Completed',
        data: [80, 55, 70, 60, 75],
        backgroundColor: '#28a745',
        borderColor: '#1e7e34',
        borderWidth: 1,
      },
      {
        label: 'Pending',
        data: [10, 5, 10, 10, 10],
        backgroundColor: '#ffc107',
        borderColor: '#e0a800',
        borderWidth: 1,
      },
    ],
  };

  // Enhanced bar chart options for better responsiveness
  const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          padding: 20,
          font: {
            size: 12
          }
        }
      },
      title: {
        display: true,
        text: 'Department-wise Employee Statistics',
        font: {
          size: 16,
          weight: 'bold'
        },
        padding: {
          top: 10,
          bottom: 30
        }
      }
    },
    scales: {
      x: {
        beginAtZero: true,
        ticks: {
          maxRotation: 45,
          minRotation: 0,
          font: {
            size: 11
          }
        },
        grid: {
          display: false
        }
      },
      y: {
        beginAtZero: true,
        ticks: {
          font: {
            size: 11
          }
        },
        grid: {
          color: '#e0e0e0'
        }
      }
    },
    interaction: {
      intersect: false,
      mode: 'index'
    },
    animation: {
      duration: 1000,
      easing: 'easeOutQuart'
    }
  };

  const pieData = {
    labels: ['Completed', 'Pending'],
    datasets: [
      {
        data: [70, 30],
        backgroundColor: ['#28a745', '#ffa500'],
        borderColor: ['#1e7e34', '#e0a800'],
        borderWidth: 2,
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 20,
          font: {
            size: 12
          }
        }
      },
      title: {
        display: true,
        text: 'Overall Assessment Status',
        font: {
          size: 16,
          weight: 'bold'
        },
        padding: {
          top: 10,
          bottom: 20
        }
      }
    },
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
    animation: {
      duration: 1000,
      easing: 'easeOutQuart'
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
            <li><Link to="/employee-list">Employee List</Link></li>
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
              <FaUsers className="stat-card-icon user" />
              <h2>Total Employees</h2>
              <p>{totalEmployees?.totalEmployees}</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaClipboardCheck className="stat-card-icon complete" />
              <h2>Completed Assessments</h2>
              <p>{completed}</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaExclamationTriangle className="stat-card-icon pending" />
              <h2>Pending Assessments</h2>
              <p>{pending}</p>
            </div>
            <div className="hr-dashboard-stat-card">
              <FaChartLine className="stat-card-icon rate" />
              <h2>Completion Rate</h2>
              <p>69.8%</p>
            </div>
          </section>

          {/* {error && (
            <p style={{ color: 'red', marginTop: '10px' }}>
              ⚠ Unable to fetch employee data. Server might be down.
            </p>
          )} */}

          <section className="hr-dashboard-chart-container fade-in-up">
            <h2 className="hr-dashboard-assessment-heading">Assessment Status</h2>
            <div className="hr-dashboard-charts-wrapper">
              <div className="hr-dashboard-chart-box hr-dashboard-bar-chart">
                <div className="chart-scroll-container">
                  <Bar data={barData} options={barOptions} />
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
          <Notification onClose={() => setNotificationOpen(false)} />
        </div>
      )}
    </>
  );
};

export default HrDashboard;