import React, { useEffect, useState } from "react";
import "./KraKpiUpload.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../modal/Modal";
// import axios from "axios"; // not needed for dummy data
// import { baseUrl } from '../../urls/CommenUrl';


const dummyData = [
  {
    kraName: "Technical Skills",
    weightage: 40,
    kpi: [
      { description: "Code Quality and Standards", weightage: 15 },
      { description: "Bug Fixing & Debugging", weightage: 10 },
      { description: "Learning New Technologies", weightage: 15 },
    ],
  },
  {
    kraName: "Team Collaboration",
    weightage: 30,
    kpi: [
      { description: "Peer Code Reviews", weightage: 10 },
      { description: "Knowledge Sharing Sessions", weightage: 10 },
      { description: "Cross-Team Support", weightage: 10 },
    ],
  },
  {
    kraName: "Productivity & Delivery",
    weightage: 30,
    kpi: [
      { description: "Meeting Project Deadlines", weightage: 15 },
      { description: "Task Ownership", weightage: 10 },
      { description: "Time Management", weightage: 5 },
    ],
  },
];

const KraKpiUpload = () => {
  const [errorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title] = useState("");
  const [krakpi, setKraKpi] = useState([]);

  /* eslint-disable no-unused-vars */
  const { id: employeeId } = useParams();
  const token = localStorage.getItem("token");
  /* eslint-enable no-unused-vars */

  const closeModal = () => {
    setShowModal(false);
  };


  useEffect(() => {
    // Instead of API call, set dummy data
    setKraKpi(dummyData);
  }, []);

  return (
    <div className="employee-module-review-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}

      <header className="employee-module-review-header">
        <div className="employee-module-review-title-section">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="employee-module-review-icon home-icon" />
          </Link>
          <h1 className="employee-module-review-title">KRA KPI Upload</h1>
          <img
            src={logo}
            alt="Company Logo"
            className="employee-module-review-company-logo"
          />
        </div>
      </header>

      <div className="employee-module-review-sections">
        {krakpi.map((kra, kraIndex) => (
          <div key={kraIndex} className="employee-module-review-section">
            <div className="employee-module-review-section-inner">
              <div className="employee-module-review-section-header">
                <h3>KRA - {kra.kraName}</h3>
                <span>Weightage: {kra.weightage}</span>
              </div>
              <table className="employee-module-review-table">
                <thead>
                  <tr>
                    <th>KPI's</th>
                    <th style={{ width: "5%" }}>Weightage</th>

                  </tr>
                </thead>
                <tbody>
                  {kra.kpi.map((kpi, kpiIndex) => (
                    <tr key={kpiIndex}>
                      <td>{kpi.description}</td>
                      <td>{kpi.weightage}</td>

                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        ))}
      </div>

      <div className="employee-module-review-actions">
        <button className="employee-module-review-submit-review-btn">
          Upload
        </button>
      </div>
    </div>
  );
};

export default KraKpiUpload;
