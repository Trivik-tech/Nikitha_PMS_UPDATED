import {React,useState} from "react";
import "./Approve.css";
import logo from "../../../assets/images/nikithas-logo.png"; // Make sure the path is correct
import { FaHome } from "react-icons/fa";
import { Link } from "react-router-dom";
import PmsApprove from '../../../components/modal/approve-pms/PmsApprove'

const Approve = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [approvalStatus, setApprovalStatus] = useState(""); 
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

  const handleApproveClick = () => {
    setApprovalStatus("approve");
    setIsModalOpen(true);
  };

  const handleRejectClick = () => {
    setApprovalStatus("reject");
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className="manager-approve-container">
      <header className="manager-approve-header">
        <div className="manager-approve-title-section">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="icon home-icon" />
          </Link>
          <h1 className="manager-approve-title">Performance Approval</h1>
          <img src={logo} alt="Company Logo" className="manager-approve-logo" />
        </div>

        <div className="manager-approve-filters">
          <label>Employee ID: <input type="text" value="EMP123" readOnly /></label>
          <label>Employee Name: <input type="text" value="John Doe" readOnly /></label>
          <label>Designation: <input type="text" value="Senior Software Engineer" readOnly /></label>
          <label>Department: <input type="text" value="Engineering" readOnly /></label>
          <label>Manager: <input type="text" value="Jane Smith" readOnly /></label>
        </div>
      </header>

      <div className="manager-approve-sections">
        {sections.map((section, index) => (
          <div key={index} className="manager-approve-section">
            <div className="manager-approve-section-header">
              <h3>KRA - {section.title}</h3>
              <span>Weightage: {section.weightage}</span>
            </div>
            <table className="manager-approve-table">
              <thead>
                <tr>
                  <th>KPI's</th>
                  <th>Weightage</th>
                </tr>
              </thead>
              <tbody>
                {section.items.map((item, idx) => (
                  <tr key={idx}>
                    <td>{item}</td>
                    <td>4</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>

      

      <div className="manager-approve-actions">
        <button className="manager-approve-submit-review-btn" onClick={handleApproveClick}>Approve</button>
        <button className="manager-approve-submit-review-btn-reject" onClick={handleRejectClick}>Reject</button>
      </div>

      {/* Modal for Approval/Rejection */}
      <PmsApprove 
        isOpen={isModalOpen} 
        onClose={closeModal} 
        status={approvalStatus} 
      />

      <div className="manager-approve-approval">
        
      </div>
    </div>
  );
};

export default Approve;
