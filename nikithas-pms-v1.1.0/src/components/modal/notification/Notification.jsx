import React, { useState } from "react";
import "./Notification.css";

const Notification = ({ onClose }) => {
  const [activeTab, setActiveTab] = useState("unread");
  
  return (
    <div className="notification-modal-overlay" onClick={onClose}>
      <div className="notification-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Notifications</h2>
          <select className="filter-dropdown">
            <option>All notifications</option>
          </select>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        
        <div className="tabs">
          <div 
            className={`tab ${activeTab === "unread" ? "active-tab" : ""}`} 
            onClick={() => setActiveTab("unread")}
          >
            Unread <span className="badge">2</span>
          </div>
          <div 
            className={`tab ${activeTab === "read" ? "active-tab" : ""}`} 
            onClick={() => setActiveTab("read")}
          >
            Read
          </div>
        </div>

        <div className="notification-list">
          {[...Array(5)].map((_, index) => (
            <div className="notification-item" key={index}>
              <div className="icon-container">
                <span role="img" aria-label="icon">📩</span>
              </div>
              <div className="notification-content">
                <strong>Notification {index + 1}</strong>
                <p>This is a sample notification message...</p>
              </div>
              <div className="notification-actions">
                <span className="timestamp">1h ago</span>
                <button className="mark-read">Mark as read</button>
              </div>
            </div>
          ))}
        </div>
        
        <div className="modal-footer">
          <button className="mark-all-read">Mark all as read</button>
          <button className="clear-all">Clear all notifications</button>
        </div>
      </div>
    </div>

  );
};

export default Notification;