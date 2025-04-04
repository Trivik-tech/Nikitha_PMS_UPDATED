import React from 'react';
import './Modal.css';  // Import the new CSS for the modal styling

const Modal = ({ message, closeModal, title }) => {
  return (
    <div className="global-modal-overlay" onClick={closeModal}>
      <div className="global-modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>{title}</h2>
        <p>{message}</p>
        <button className="global-close-btn" onClick={closeModal}>Close</button>
      </div>
    </div>
  );
};

export default Modal;
