import React from "react";
import "./PmsInitiated.css";

const PmsInitiated = ({ onClose, employeeName }) => {
  return (
    <div className="pms-modal-overlay">
      <div className="pms-modal">
        <h3>PMS Initiated Successfully</h3>
        <p>The performance management system has been initiated for <strong>{employeeName}</strong>.</p>
        <button className="pms-modal-close" onClick={onClose}>Close</button>
      </div>
    </div>
  );
};

export default PmsInitiated;
