import React, { useState } from "react";
import "./Notification.css";

const Notification = ({ onClose, notifications = [] }) => {
  const [activeTab, setActiveTab] = useState("unread");
  const [selectedNotification, setSelectedNotification] = useState(null);

  const handleNotificationClick = (notification) => {
    setSelectedNotification(notification);
  };

  const closeDetailsPopup = () => {
    setSelectedNotification(null);
  };

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
          <div className={`tab ${activeTab === "unread" ? "active-tab" : ""}`} onClick={() => setActiveTab("unread")}>
            Unread <span className="badge">{notifications.length}</span>
          </div>
          <div className={`tab ${activeTab === "read" ? "active-tab" : ""}`} onClick={() => setActiveTab("read")}>
            Read
          </div>
        </div>

        <div className="notification-list">
          {notifications.length === 0 ? (
            <div style={{ textAlign: "center", padding: "20px", color: "#888" }}>
              No notifications yet.
            </div>
          ) : (
            notifications.map((notification) => (
              <div
                className="notification-item"
                key={notification.id}
                onClick={() => handleNotificationClick(notification)}
              >
                <div className="icon-container">📩</div>
                <div className="notification-content">
                  <strong>{notification.title}</strong>
                  <p>{notification.message.slice(0, 50)}...</p>
                </div>
                <span className="timestamp">{notification.timestamp}</span>
              </div>
            ))
          )}
        </div>

        {selectedNotification && (
          <div className="notification-details-popup">
            <div className="details-content">
              <h3>{selectedNotification.title}</h3>
              <p>{selectedNotification.message}</p>
              <button className="close-details-btn" onClick={closeDetailsPopup}>Close</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Notification;
