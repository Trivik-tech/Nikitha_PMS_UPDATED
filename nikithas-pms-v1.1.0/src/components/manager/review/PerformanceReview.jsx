import { React, useState } from "react";
import "./PerformanceReview.css";
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

  const printHandler=()=>{
    // Placeholder for print functionality
  }

  return (
    <div className="prf-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}
      <header className="prf-header">
        <div className="prf-title-section">
          {/* Home icon on the left */}
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="per-icon home-icon" />
          </Link>

          {/* Centered Title */}
          <h1 className="prf-title">Performance Review Form</h1>

          {/* Logo on the right */}
          <img src={logo} alt="Company Logo" className="prf-company-logo" />
        </div>

        <div className="prf-filters">
          <label>
            Due Date: <input type="text" value="20/3/2025" readOnly/>
          </label>
          <label>
            Self Review Date: <input type="text" value="14/3/2025" readOnly/>
          </label>
          <label>
            Manager Review Date: <input type="text" readOnly />
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

      <div className="prf-sections">
        {sections.map((section, index) => (
          <div key={index} className="prf-section">
            <div className="prf-section-header">
              <h3>KRA - {section.title}</h3>
              <span>Weightage: {section.weightage}</span>
            </div>
            <table className="prf-table">
              <thead>
                <tr>
                  <th>KPI's</th>
                  <th>Weightage</th>
                  <th>Self Rating</th>
                  <th>
                    <span style={{ color: "red", fontSize: "20px" }}>*</span>
                    Review-1
                  </th>

                  <th>Review-2</th>
                  <th>Average</th>
                </tr>
              </thead>
              <tbody>
                {section.items.map((item, idx) => (
                  <tr key={idx}>
                    <td>{item}</td>
                    <td>4</td>
                    <td>
                      <input type="text" />
                    </td>
                    <td>
                      <input type="text" />
                    </td>
                    <td>
                      <input type="text" />
                    </td>
                    <td>
                      <input type="text" readOnly />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>

      <div className="prf-overall-section">
        <div className="prf-overall-score">
          <h3>Overall Score</h3>
          <table className="prf-overall-table">
            <thead>
              <tr>
                <th>Category</th>
                <th>Self Score</th>

                <th>Review 1</th>
                <th>Review 2</th>
                <th>Final Score</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Total Score</td>
                <td>80.5</td>

                <td>--</td>
                <td>--</td>
                <td>--</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="prf-overall-remarks">
          <h3>Overall Remarks</h3>
          <label>Overall Remarks</label>
          <textarea placeholder="Enter your overall remarks..."></textarea>
        </div>

        <div className="prf-actions">
        <button className="prf-print-btn" onClick={printHandler}>
            Print
          </button>
          <button className="prf-draft-btn" onClick={draftSave}>
            Save as Draft
          </button>
          <button className="prf-submit-review-btn" onClick={reviewSubmit}>
            Submit Review
          </button>
        </div>
      </div>
    </div>
  );
};

export default PerformanceReview;
