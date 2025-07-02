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
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const jwtToken = localStorage.getItem("token");

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

          const updated = [newMsg, ...notifications];

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

            const filteredRecent = formattedRecent.filter(msg => {
              const msgTime = Math.floor(new Date(msg.timestamp).getTime() / 1000);
              const newMsgTime = Math.floor(new Date(newMsg.timestamp).getTime() / 1000);
              return !(msgTime === newMsgTime && msg.message === newMsg.message);
            });

            const combined = [...updated, ...filteredRecent];
            const unique = Array.from(
              new Map(
                combined.map(msg => [
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
  }, []);

  return (
    <div className="employee-dashboard-container">
      {/* Sidebar and overlay */}
      <div className={`employee-dashboard-sidebar-overlay ${sidebarOpen ? 'visible' : 'hidden'}`}
        onClick={() => setSidebarOpen(false)}
        style={{ display: sidebarOpen ? 'block' : 'none' }}
      />
      <div className="employee-dashboard-hamburger" onClick={() => setSidebarOpen(!sidebarOpen)}>
        <span /><span /><span />
      </div>

      {/* Bell Icon */}
      <div className="employee-dashboard-bell-topright"
        onClick={() => setNotificationOpen(!notificationOpen)}
      >
        <Bell className="employee-dashboard-notificationButton" />
        {notifications.length > 0 && (
          <span className="notif-count">{notifications.length}</span>
        )}
      </div>

      {/* Sidebar */}
      <aside className={`employee-dashboard-sidebar${sidebarOpen ? ' open' : ''}`}>
        <div className="employee-profile-container">
          <img src={employeeData.profileImage} alt="Profile" className="employee-profile-img" />
          <h2 className="employee-name">{employeeData.name}</h2>
        </div>
        <ul>
          <li><Link to="/employee-dashboard" className="active" onClick={() => setSidebarOpen(false)}>My Dashboard</Link></li>
          <li><Link to={`/self-review/${employeeData.id}`} onClick={() => setSidebarOpen(false)}>Start PMS</Link></li>
          <li><Link to="/employee-performance" onClick={() => setSidebarOpen(false)}>My Performance</Link></li>
          <li><Link to="/add-krakpi" onClick={() => setSidebarOpen(false)}>Add KRA|KPI</Link></li>
        </ul>
        <Link to="/" className="employee-sidebar-logout-btn">Logout</Link>
      </aside>

      {/* Header */}
      <main className="employee-main-content">
        <header className="employee-dashboard-header">
          <div className="employee-dashboard-title">Employee Dashboard</div>
          <div className="employee-dashboard-logo"><img src={logo} alt="Nikitha PMS" /></div>
          <div className="employee-dashboard-actions">
            <Bell className="employee-dashboard-notificationButton employee-dashboard-bell-desktop"
              onClick={() => setNotificationOpen(!notificationOpen)}
            />
            {notifications.length > 0 && (
              <span className="notif-count">{notifications.length}</span>
            )}
            <Link to="/" className="employee-logout-btn">Logout</Link>
          </div>
        </header>

        {/* Main Info */}
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

      {/* Notification modal */}
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