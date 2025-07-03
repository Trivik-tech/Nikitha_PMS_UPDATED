import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './EmployeeDashboard.css';
import './EmployeeDashboardResponsive.css';
import logo from '../../../assets/images/nikithas-logo.png';
import { Bell } from 'lucide-react';
import Notification from '../../modal/notification/Notification';
import axios from 'axios';
import { baseUrl } from '../../urls/CommenUrl';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const employeeData = {
  id: 'EMP1235',
  name: 'Sowmya K',
  location: 'Tumkur, Karnataka',
  email: 'Sowmya74@gmail.com',
  profileImage: 'https://assets.onecompiler.app/4344tsra5/43brqemzn/1000115242.jpg',
  phone: '+91 9448576762',
  department: 'HR Department',
  manager: {
    name: 'Ravindra Kulkarni',
    position: 'VP of Product',
    image: 'https://assets.onecompiler.app/4344tsra5/43brpvuah/1000115238.jpg',
  }
};

const EmployeeDashboard = () => {
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0); // ✅ NEW: Notification badge count
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [newAndUndelivered, setNewAndUndelivered] = useState([]);
  const jwtToken = localStorage.getItem("token");

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
setNotificationOpen(true);
          setNewAndUndelivered(prev => [newMsg, ...prev]);
setNotificationCount(prev => prev + 1); // ✅ INCREASE count for red badge
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

    client.activate();
    return () => client.deactivate();
    // eslint-disable-next-line
  }, []);

  const handleNotificationToggle = () => {
  setNotificationOpen(!notificationOpen);
  if (!notificationOpen) {
    setNewAndUndelivered([]);
    setNotificationCount(0); // ✅ RESET red badge when opened
  }
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
         {notificationCount > 0 && (
  <span className="notif-count">{notificationCount}</span>
)}
        </div>
      </div>

      {/* Sidebar */}
      <aside className={`employee-dashboard-sidebar${sidebarOpen ? ' open' : ''}`}>
        <div className="employee-profile-container">
          <img src={employeeData.profileImage} alt="Profile" className="employee-profile-img" />
          <h2 className="employee-name">{employeeData.name}</h2>
        </div>
        <ul>
          <li>
            <Link to="/employee-dashboard" className="active" onClick={handleSidebarClose}>My Dashboard</Link>
          </li>
          <li>
            <Link to={`/self-review/${employeeData.id}`} onClick={handleSidebarClose}>Start PMS</Link>
          </li>
          <li>
            <Link to="/employee-performance" onClick={handleSidebarClose}>My Performance</Link>
          </li>
          <li>
            <Link to={`/add-krakpi/${employeeData.id}`} onClick={handleSidebarClose}>Add KRA|KPI</Link>
          </li>
        </ul>
        <Link to="/" className="employee-sidebar-logout-btn">Logout</Link>
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
              {notificationCount > 0 && (
  <span className="notif-count">{notificationCount}</span>
)}
            </div>
            <Link to="/" className="employee-logout-btn">Logout</Link>
          </div>
        </header>

        {/* Info Section */}
        <section className="employee-info-container">
          <div className="employee-info">
            <h3 className='employee-dashboard-info-h3'>Primary Details</h3>
            <p><strong>Employee ID:</strong> {employeeData.id}</p>
            <p><strong>Employee Name:</strong> {employeeData.name}</p>
            <p><strong>Location:</strong> {employeeData.location}</p>
          </div>

          <div className="reporting-manager">
            <h3 className='employee-dashboard-info-h3'>Reporting Manager</h3>
            <div className="manager-info">
              <img src={employeeData.manager.image} alt="Manager" className="manager-img" />
              <div>
                <p><strong>Reports to:</strong> {employeeData.manager.name}</p>
                <p>{employeeData.manager.position}</p>
                <button className="employee-module-contact-manager-btn">Contact Manager</button>
              </div>
            </div>
          </div>

          <div className="employee-info">
            <h3 className='employee-dashboard-info-h3'>Contact Information</h3>
            <p><strong>Email:</strong> {employeeData.email}</p>
            <p><strong>Phone:</strong> {employeeData.phone}</p>
            <p><strong>Department:</strong> {employeeData.department}</p>
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