import React from "react";
import "./PmsApprove.css"; // Import the modal styles

const PmsApprove = ({ isOpen, onClose, status }) => {
  if (!isOpen) return null;

  return (
    <div className="pms-modal-overlay" onClick={onClose}>
      <div className="pms-modal-container" onClick={(e) => e.stopPropagation()}>
        <div className="pms-modal-header">
          <h2>{status === "approve" ? "PMS Approved" : "PMS Rejected"}</h2>
        </div>
        <div className="pms-modal-body">
          <p>
            The performance management system (PMS) for this employee has been{" "}
            {status === "approve" ? "approved" : "rejected"} successfully.
          </p>
        </div>
        <div className="pms-modal-actions">
          <button className="pms-modal-close-btn" onClick={onClose}>
            Ok
          </button>
        </div>
      </div>
    </div>
  );
};

export default PmsApprove;
