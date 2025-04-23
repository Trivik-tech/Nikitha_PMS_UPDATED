import React, { useEffect, useState } from "react";
import "./DeleteConfirmation.css";

const DeleteConfirmation = ({ isOpen, onClose, onConfirm , name,id}) => {
  const [isDeleted, setIsDeleted] = useState(false);

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
    await onConfirm(); // Calls parent deleteEmployee()
    setIsDeleted(true); // Triggers message and auto-close
  };

  if (!isOpen) return null;

  return (
    <div className="delete-modal-overlay">
      <div className="delete-modal-content">
        {!isDeleted ? (
          <>
            <h2>Confirm Delete</h2>
            <p>Are you sure you want to delete <strong>{name} ({id})</strong></p>
            <div className="delete-modal-buttons">
              <button className="cancel-btn" onClick={onClose}>Cancel</button>
              <button className="confirm-btn" onClick={handleConfirm}>Delete</button>
            </div>
          </>
        ) : (
          <div className="delete-success-message">
            <h3>Employee deleted successfully</h3>
          </div>
        )}
      </div>
    </div>
  );
};

export default DeleteConfirmation;
