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
  id: 'EMP00123',
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
  const [allMessages, setAllMessages] = useState([]);
  const [newAndUndelivered, setNewAndUndelivered] = useState([]);

  const jwtToken = 'PASTE_YOUR_EMPLOYEE_JWT_TOKEN_HERE';

  useEffect(() => {
    const socket = new SockJS(`http://localhost:8080/ws?token=${jwtToken}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: async () => {
        client.subscribe('/user/queue/employee-notification', async (message) => {
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
            console.error('Error fetching recent:', err);
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
          console.error('Initial fetch error:', err);
        }
      }
    });

    client.activate();
    return () => client.deactivate();
  }, []);

  const handleSidebarClose = () => setSidebarOpen(false);

  return (
    <div className="employee-dashboard-container">
      <div
        className={`employee-dashboard-sidebar-overlay ${sidebarOpen ? 'visible' : 'hidden'}`}
        onClick={handleSidebarClose}
        aria-label="Close sidebar"
        tabIndex={sidebarOpen ? 0 : -1}
        style={{ display: sidebarOpen ? 'block' : 'none' }}
      />
      <div
        className="employee-dashboard-hamburger"
        onClick={() => setSidebarOpen(!sidebarOpen)}
        aria-label="Open sidebar"
        tabIndex={0}
        role="button"
      >
        <span />
        <span />
        <span />
      </div>
      <div
        className="employee-dashboard-bell-topright"
        onClick={() => {
          setNotificationOpen(!notificationOpen);
          if (!notificationOpen) setNewAndUndelivered([]);
        }}
        aria-label="Show notifications"
        tabIndex={0}
        role="button"
      >
        <Bell className="employee-dashboard-notificationButton" />
        {newAndUndelivered.length > 0 && (
          <span className="notif-count">{newAndUndelivered.length}</span>
        )}
      </div>

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
            <Link to={`/self-review/CNT139`} onClick={handleSidebarClose}>Start PMS</Link>
          </li>
          <li>
            <Link to="/employee-performance" onClick={handleSidebarClose}>My Performance</Link>
          </li>
          <li>
            <Link to="/add-krakpi" onClick={handleSidebarClose}>Add KRA|KPI</Link>
          </li>
        </ul>
        <Link to="/" className="employee-sidebar-logout-btn">Logout</Link>
      </aside>

      <main className="employee-main-content">
        <header className="employee-dashboard-header">
          <div className="employee-dashboard-title">Employee Dashboard</div>
          <div className="employee-dashboard-logo">
            <img src={logo} alt="Nikitha PMS" />
          </div>
          <div className="employee-dashboard-actions">
            <Bell
              className="employee-dashboard-notificationButton employee-dashboard-bell-desktop"
              onClick={() => {
                setNotificationOpen(!notificationOpen);
                if (!notificationOpen) setNewAndUndelivered([]);
              }}
            />
            {newAndUndelivered.length > 0 && (
              <span className="notif-count">{newAndUndelivered.length}</span>
            )}
            <Link to="/" className="employee-logout-btn">Logout</Link>
          </div>
        </header>

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

      {notificationOpen && (
        <Notification onClose={() => setNotificationOpen(false)} allMessages={allMessages} />
      )}
    </div>
  );
};

export default EmployeeDashboard;