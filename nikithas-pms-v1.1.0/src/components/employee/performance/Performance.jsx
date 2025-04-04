import React from 'react';
import './Performance.css';
import logo from '../../../assets/images/nikithas-logo.png';
import { FaHome } from 'react-icons/fa'; // Importing the home icon
import { Link } from 'react-router-dom';

const Performance = () => {
  const handlePrint = () => {
    window.print(); // This will trigger the browser's print dialog
  };

  return (
    <div className="employee-module-container">
      <div className="employee-module-header">
        <div className="employee-module-home-icon">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="per-icon home-icon" />
          </Link>
        </div>
        <h1>Performance Summary</h1>
        <img src={logo} alt="Logo" className='employee-module-company-logo' />
      </div>

      {/* KRA Section for 2023 */}
      <div className="employee-module-section">
        <h2>Key Result Areas (KRA) - 2023</h2>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Project Delivery</div>
          <div className="employee-module-kra-details">
            Completed multiple critical projects with good quality and minimal delays.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 85/100</span>
            <span>Manager Rating: 82/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Technical Skills</div>
          <div className="employee-module-kra-details">
            Showed consistent growth in technical capabilities throughout the year.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 75/100</span>
            <span>Manager Rating: 78/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Team Collaboration</div>
          <div className="employee-module-kra-details">
            Demonstrated strong teamwork and effective communication skills.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 81/100</span>
            <span>Manager Rating: 75/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Innovation & Initiative</div>
          <div className="employee-module-kra-details">
            Demonstrated innovative solutions and led initiatives to improve processes.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 89/100</span>
            <span>Manager Rating: 92/100</span>
          </div>
        </div>

        <div className="employee-module-progress-bar">
          <span className="employee-module-self-rating"></span>
          <span className="employee-module-manager-rating"></span>
        </div>

        <div className="employee-module-blue-progress">
          <span></span>
        </div>

        <div className="employee-module-overall-performance">
          <span>Average Self Rating: 86/100</span>
          <span>Average Manager Rating: 82/100</span>
        </div>
      </div>

      {/* KRA Section for 2024 */}
      <div className="employee-module-section">
        <h2>Key Result Areas (KRA) - 2024</h2>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Project Delivery</div>
          <div className="employee-module-kra-details">
            Successfully delivered all assigned projects within deadline with high quality standards.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 90/100</span>
            <span>Manager Rating: 94/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Technical Skills</div>
          <div className="employee-module-kra-details">
            Demonstrated strong technical expertise and problem-solving abilities.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 81/100</span>
            <span>Manager Rating: 80/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Team Collaboration</div>
          <div className="employee-module-kra-details">
            Excelled in both strong communication and collaboration skills.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 96/100</span>
            <span>Manager Rating: 95/100</span>
          </div>
        </div>

        <div className="employee-module-kra">
          <div className="employee-module-kra-title">Innovation & Initiative</div>
          <div className="employee-module-kra-details">
            Successfully introduced innovative solutions and led improvement initiatives.
          </div>
          <div className="employee-module-rating">
            <span>Self Rating: 85/100</span>
            <span>Manager Rating: 92/100</span>
          </div>
        </div>

        <div className="employee-module-progress-bar">
          <span className="employee-module-self-rating"></span>
          <span className="employee-module-manager-rating"></span>
        </div>

        <div className="employee-module-blue-progress">
          <span></span>
        </div>

        <div className="employee-module-overall-performance">
          <span>Average Self Rating: 86/100</span>
          <span>Average Manager Rating: 82/100</span>
        </div>
      </div>

      {/* Print Button */}
      <button className="employee-module-print-btn" onClick={handlePrint}>
        Print
      </button>
    </div>
  );
};

export default Performance;
