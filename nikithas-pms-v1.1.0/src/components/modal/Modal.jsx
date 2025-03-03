import React from 'react';

const Modal = ({ message, closeModal ,title}) => {
    return (
        <div className="login-modal-overlay">
            <div className="login-modal-content">
                <h2>{title}</h2>
                <p>{message}</p>
                <button onClick={closeModal}>Close</button>
            </div>
        </div>
    );
};

export default Modal;
