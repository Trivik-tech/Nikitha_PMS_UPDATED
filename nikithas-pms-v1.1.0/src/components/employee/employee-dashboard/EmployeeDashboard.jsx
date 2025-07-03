import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './EmployeeDashboard.css';
import './EmployeeDashboardResponsive.css';
import logo from '../../../assets/images/nikithas-logo.png';
import defaultProfile from '../../../assets/images/profile1.jpg' // <-- Add this import // <-- Add this import
import { Bell } from 'lucide-react';
import Notification from '../../modal/notification/Notification';
import axios from 'axios';
import { baseUrl } from '../../urls/CommenUrl';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const EmployeeDashboard = () => {
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [newAndUndelivered, setNewAndUndelivered] = useState([]);
  const [employeeData, setEmployeeData] = useState(null);
  const jwtToken = localStorage.getItem("token");

  const navigate=useNavigate()

  // Helper for closing sidebar
  const handleSidebarClose = () => {
    setSidebarOpen(false);
  };

  useEffect(() => {
    const socket = new SockJS(`http://localhost:8080/ws?token=${jwtToken}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,

      onConnect: async () => {
        client.subscribe('/user/queue/employee-notification', async (message) => {
          const newMsg = {
            title: "New Notification",
            message: message.body,
            timestamp: new Date().toISOString()
          };

          setNewAndUndelivered(prev => [newMsg, ...prev]);

          try {
            const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
              headers: { Authorization: `Bearer ${jwtToken}` }
            });

            const recent = res.data;
            const formattedRecent = recent.map(msg => ({
              title: "Recent Notification",
              message: msg.message || msg.content || "No content",
              timestamp: msg.timestamp || new Date().toISOString()
            }));

            const allMessages = [newMsg, ...formattedRecent];

            const unique = Array.from(
              new Map(
                allMessages.map(msg => [
                  `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.message}`,
                  msg
                ])
              ).values()
            );

            setNotifications(unique.slice(0, 50));
          } catch (err) {
            console.error("❌ Error fetching recent:", err);
          }
        });

        // Initial fetch
        try {
          const res = await axios.get(`${baseUrl}/api/v1/pms/recent`, {
            headers: { Authorization: `Bearer ${jwtToken}` }
          });

          const recent = res.data;
          const formatted = recent.map(msg => ({
            title: "Recent Notification",
            message: msg.message || msg.content || "No content",
            timestamp: msg.timestamp || new Date().toISOString()
          }));

          const unique = Array.from(
            new Map(
              formatted.map(msg => [
                `${Math.floor(new Date(msg.timestamp).getTime() / 1000)}-${msg.message}`,
                msg
              ])
            ).values()
          );

          setNotifications(unique.slice(0, 50));
        } catch (err) {
          console.error("❌ Initial fetch error:", err);
        }
      }
    });

    loadProfile();
    client.activate();
    return () => client.deactivate();
    // eslint-disable-next-line

  }, []);

  const handleNotificationToggle = () => {
    setNotificationOpen(!notificationOpen);
    if (!notificationOpen) setNewAndUndelivered([]);
  };

  const loadProfile = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/employee/profile`, {
        headers: {
          Authorization: `Bearer ${jwtToken}`
        }
      });
      setEmployeeData(result.data);
    } catch (error) {
      console.log(error)
    }
  }

  // Helper to format date
  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
  };

  return (
    <div className="employee-dashboard-container">
      {/* Sidebar Overlay */}
      <div
        className={`employee-dashboard-sidebar-overlay ${sidebarOpen ? 'visible' : 'hidden'}`}
        onClick={handleSidebarClose}
        style={{ display: sidebarOpen ? 'block' : 'none' }}
      />

      {/* Hamburger */}
      <div className="employee-dashboard-hamburger" onClick={() => setSidebarOpen(!sidebarOpen)}>
        <span /><span /><span />
      </div>

      {/* Notification Bell (Mobile) */}
      <div
        className="employee-dashboard-bell-topright"
        onClick={handleNotificationToggle}
        aria-label="Show notifications"
        tabIndex={0}
        role="button"
      >
        <div style={{ position: 'relative' }}>
          <Bell className="employee-dashboard-notificationButton" />
          {newAndUndelivered.length > 0 && (
            <span className="notif-count">{newAndUndelivered.length}</span>
          )}
        </div>
      </div>

      {/* Sidebar */}
      <aside className={`employee-dashboard-sidebar${sidebarOpen ? ' open' : ''}`}>
        <div className="employee-profile-container">
          <img
            src={employeeData?.profileImage ? employeeData.profileImage : defaultProfile}
            alt="Profile"
            className="employee-profile-img"
          />
          <h2 className="employee-name">{employeeData?.name}</h2>
        </div>
        <ul>
          <li>
            <Link to="/employee-dashboard" className="active" onClick={handleSidebarClose}>My Dashboard</Link>
          </li>
          <li>
            <Link to={`/self-review/${employeeData?.empId}`} onClick={handleSidebarClose}>Start PMS</Link>
          </li>
          <li>
            <Link to="/employee-performance" onClick={handleSidebarClose}>My Performance</Link>
          </li>
          <li>
            <Link to={`/add-krakpi/${employeeData?.empId}`} onClick={handleSidebarClose}>Add KRA|KPI</Link>
          </li>
        </ul>
        <button className="employee-sidebar-logout-btn" 
         onClick={() => {
                  localStorage.clear();
                  navigate("/");
                }}
        >Logout</button>
      </aside>

      {/* Main Content */}
      <main className="employee-main-content">
        <header className="employee-dashboard-header">
          <div className="employee-dashboard-title">Employee Dashboard</div>
          <div className="employee-dashboard-logo"><img src={logo} alt="Nikitha PMS" /></div>
          <div className="employee-dashboard-actions">
            <div style={{ position: 'relative' }}>
              <Bell
                className="employee-dashboard-notificationButton employee-dashboard-bell-desktop"
                onClick={handleNotificationToggle}
              />
              {newAndUndelivered.length > 0 && (
                <span className="notif-count">{newAndUndelivered.length}</span>
              )}
            </div>
            <button className="employee-logout-btn" 
             onClick={() => {
                  localStorage.clear();
                  navigate("/");
                }}
            >Logout</button>
          </div>
        </header>

        {/* Info Section */}
        <section className="employee-info-container">
          <div className="employee-info">
            <h3 className='employee-dashboard-info-h3'>Primary Details</h3>
            <p><strong>Employee ID:</strong> {employeeData?.empId}</p>
            <p><strong>Employee Name:</strong> {employeeData?.name}</p>
            <p><strong>Current Designation:</strong> {employeeData?.currentDesignation}</p>
           
            <p><strong>Department:</strong> {employeeData?.department}</p>
            
          </div>

          <div className="reporting-manager">
            <h3 className='employee-dashboard-info-h3'>Reporting Manager</h3>
            <div className="manager-info">
              <img
                src={defaultProfile}
                alt="Manager"
                className="manager-img"
              />
              <div>
                <p><strong>Reports to:</strong> {employeeData?.reportingManager}</p>
                <button className="employee-module-contact-manager-btn">Contact Manager</button>
              </div>
            </div>
          </div>

          <div className="employee-info">
            <h3 className='employee-dashboard-info-h3'>Contact Information</h3>
            <p><strong>Email:</strong> {employeeData?.emailId}</p>
            <p><strong>Official Email ID:</strong> {employeeData?.officialEmailId}</p>
            <p><strong>Phone:</strong> {employeeData?.mobileNumber}</p>
          </div>
        </section>
      </main>

      {/* Notification Modal */}
      {notificationOpen && (
        <Notification
          onClose={() => setNotificationOpen(false)}
          notifications={notifications}
        />
      )}
    </div>
  );
};

export default EmployeeDashboard;