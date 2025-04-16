import { React, useState } from 'react';
import { Link } from 'react-router-dom';
import './EmployeeDashboard.css';
import logo from '../../../assets/images/nikithas-logo.png';
import { Bell } from 'lucide-react';
import Notification from '../../modal/notification/Notification';

const employeeData = {
  id: 'EMP00123',
  name: 'Sowmya Kumari',
  location: 'Tumkur, Karnataka',
  email: 'Sowmya74@gmail.com',
  profileImage: 'https://assets.onecompiler.app/4344tsra5/43brqemzn/1000115242.jpg',
  phone: '+91 9448576762',
  department: 'HR Department',
  manager: {
    name: 'Ravin',
    position: 'VP of Product',
    image: 'https://assets.onecompiler.app/4344tsra5/43brpvuah/1000115238.jpg',
  }
};

const EmployeeDashboard = () => {
  const [notificationOpen, setNotificationOpen] = useState(false);
  return (
    <div className="employee-dashboard-container">
      {notificationOpen && <Notification onClose={() => setNotificationOpen(false)} />}
      {/* Sidebar */}
      <aside className="employee-dashboard-sidebar">
        <div className="employee-profile-container">
          <img src={employeeData.profileImage} alt="Profile" className="employee-profile-img" />
          <h2 className="employee-name">{employeeData.name}</h2>
        </div>
        <ul>
          <li><Link to="/employee-dashboard" className="active">My Dashboard</Link></li>
          <li><Link to="/self-review">Start PMS</Link></li>
          <li><Link to="/employee-performance">My Performance</Link></li>
          <li><Link to="/add-krakpi">Register KRA|KPI</Link></li>
        </ul>
      </aside>

      {/* Main Content */}
      <main className="employee-main-content">
        <header className="employee-dashboard-header">
          <div className="employee-dashboard-title">Employee Dashboard</div>
          <div className="employee-dashboard-logo">
            <img src={logo} alt="Nikitha PMS" />
          </div>
          <div className="employee-dashboard-actions">
            <Bell className="employee-dashboard-notificationButton" onClick={() => setNotificationOpen(!notificationOpen)} />
            
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
                <button className="contact-manager-btn">Contact Manager</button>
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
    </div>
  );
};

export default EmployeeDashboard;
