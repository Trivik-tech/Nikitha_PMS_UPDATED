import React from "react";
import "./FileConfirmation.css";

const FileConfirmation = ({ file, onConfirm, onCancel }) => {
  return (
    <div className="file-confirmation-modal-overlay">
      <div className="file-confirmation-modal">
        <h2 className="modal-title">Confirm File Upload</h2>
        <div className="modal-content">
          <p><strong>Selected File:</strong> {file.name}</p>
          <p><strong>File Size:</strong> {Math.round(file.size / 1024)} KB</p>
        </div>
        <div className="modal-buttons">
          <button onClick={onConfirm} className="modal-button confirm">Confirm</button>
          <button onClick={onCancel} className="modal-button cancel">Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default FileConfirmation;
