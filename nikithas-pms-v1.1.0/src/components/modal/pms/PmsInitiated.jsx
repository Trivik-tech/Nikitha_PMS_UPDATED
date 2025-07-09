import React from "react";
import "./PmsInitiated.css";

const PmsInitiated = ({ onClose, employeeName, employeeId }) => {
  return (
    <div className="pms-modal-overlay" onClick={onClose}>
      <div className="pms-modal" onClick={(e) => e.stopPropagation()}>
        <div className="pms-modal-header">
        </div>
        <div className="pms-modal-body">
          <p>
            The performance management system has been initiated for{" "}
            <strong>{employeeName}</strong> ({employeeId}).
          </p>
        </div>
        <button className="pms-modal-close" onClick={onClose}>
          Close
        </button>
      </div>
    </div>
  );
};

export default PmsInitiated;
