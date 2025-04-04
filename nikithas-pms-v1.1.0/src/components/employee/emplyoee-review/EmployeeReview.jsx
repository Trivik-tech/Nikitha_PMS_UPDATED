import { React, useState } from "react";
import "./EmployeeReview.css";
import logo from "../../../assets/images/nikithas-logo.png"; // Make sure the path is correct
import { FaHome } from "react-icons/fa";
import { Link } from "react-router-dom";
import Modal from "../../modal/Modal";

const PerformanceReview = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const sections = [
    {
      title: "Technical Excellence",
      weightage: 20,
      items: [
        "Code Quality & Standards",
        "Code Documentation",
        "Technical Documentation",
        "POC & Demonstrations",
        "Technical Presentations",
      ],
    },
    {
      title: "Project Delivery",
      weightage: 25,
      items: [
        "Delivery Milestones",
        "Sprint Execution",
        "Release Readiness",
        "Process Compliance",
      ],
    },
    {
      title: "Team Collaboration",
      weightage: 25,
      items: [
        "Knowledge Sharing",
        "Inter/Intra Team Collaboration",
        "Peer Reviews",
        "Conflict Resolution",
      ],
    },
    {
      title: "Leadership & Mentoring",
      weightage: 15,
      items: ["Mentoring Team Members", "Providing Feedback", "Ownership"],
    },
    {
      title: "Professional Development",
      weightage: 10,
      items: ["Learning & Development"],
    },
  ];

  const reviewSubmit = () => {
    setErrorMessage("PMS Review is successfully completed.");
    setTitle("PMS Review");
    setShowModal(true);
  };
  const draftSave = () => {
    setErrorMessage("PMS Review has been saved as a draft.");
    setTitle("PMS Review");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const printHandler = () => {
    // Placeholder for print functionality
  };

  return (
    <div className="employee-module-review-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}
      <header className="employee-module-review-header">
        <div className="employee-module-review-title-section">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="employee-module-review-icon home-icon" />
          </Link>
          <h1 className="employee-module-review-title">Performance Review Form</h1>
          <img
            src={logo}
            alt="Company Logo"
            className="employee-module-review-company-logo"
          />
        </div>

        <div className="employee-module-review-filters">
          <label>
            Due Date: <input type="text" value="20/3/2025" readOnly />
          </label>
          <label>
            Self Review Date: <input type="text" value="14/3/2025" readOnly />
          </label>
          <label>
            Employee Name: <input type="text" value="Avinash SH" readOnly />
          </label>
          <label>
            Designation:
            <input type="text" value="Senior Software Engineer" readOnly />
          </label>
          <label>
            Department: <input type="text" value="Engineering" readOnly />
          </label>
        </div>
      </header>

      <div className="employee-module-review-sections">
        {sections.map((section, index) => (
          <div key={index} className="employee-module-review-section">
            <div className="employee-module-review-section-header">
              <h3>KRA - {section.title}</h3>
              <span>Weightage: {section.weightage}</span>
            </div>
            <table className="employee-module-review-table">
              <thead>
                <tr>
                  <th>KPI's</th>
                  <th style={{ width: "5%" }}>Weightage</th>
                  <th style={{ width: "5%" }}>Self Rating</th>
                </tr>
              </thead>
              <tbody>
                {section.items.map((item, idx) => (
                  <tr key={idx}>
                    <td>{item}</td>
                    <td>4</td>
                    <td><input type="text" /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>

      <div className="employee-module-review-actions">
        <button className="employee-module-review-print-btn" onClick={printHandler}>Print</button>
        <button className="employee-module-review-draft-btn" onClick={draftSave}>Save as Draft</button>
        <button className="employee-module-review-submit-review-btn" onClick={reviewSubmit}>Submit Review</button>
      </div>
    </div>
  );
};

export default PerformanceReview;
