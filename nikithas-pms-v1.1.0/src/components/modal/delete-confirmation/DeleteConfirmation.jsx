import React, { useEffect, useState } from "react";
import "./DeleteConfirmation.css";

const DeleteConfirmation = ({ isOpen, onClose, onConfirm, name, id }) => {
  const [isDeleted, setIsDeleted] = useState(false);
  const [inactiveDate, setInactiveDate] = useState("");

  useEffect(() => {
    let timer;
    if (isDeleted) {
      timer = setTimeout(() => {
        setIsDeleted(false);
        onClose();
      }, 1500); // Auto close after 1.5 seconds
    }
    return () => clearTimeout(timer);
  }, [isDeleted, onClose]);

  const handleConfirm = async () => {
    if (!inactiveDate) {
      alert("Please select the last working day.");
      return;
    }
    await onConfirm(inactiveDate); // ✅ Pass selected date
    setIsDeleted(true);
  };

  if (!isOpen) return null;

  return (
    <div className="delete-modal-overlay">
      <div className="delete-modal-content">
        {!isDeleted ? (
          <>
            <p>
              Are you sure you want to exit <strong>{name} ({id})</strong> from
              the company?
            </p>

            <div className="date-input-container">
              <label htmlFor="inactiveDate">Select last working day</label>
              <input
                type="date"
                id="inactiveDate"
                value={inactiveDate}
                onChange={(e) => setInactiveDate(e.target.value)}
              />
            </div>

            <div className="delete-modal-buttons">
              <button className="cancel-btn" onClick={onClose}>
                Cancel
              </button>
              <button className="confirm-btn" onClick={handleConfirm}>
                Confirm Exit
              </button>
            </div>
          </>
        ) : (
          <div className="delete-success-message">
            <h3>Employee exited successfully on {inactiveDate}</h3>
          </div>
        )}
      </div>
    </div>
  );
};

export default DeleteConfirmation;
