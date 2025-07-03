import React, { useState } from "react";
import "./Notification.css";

const Notification = ({ onClose, notifications = [] }) => {
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
          <h2>
            <span role="img" aria-label="bell" style={{ marginRight: 8 }}>🔔</span>
            Notifications
          </h2>
          <select className="filter-dropdown">
            <option>All notifications</option>
          </select>
          <button className="close-btn" onClick={onClose} title="Close">×</button>
        </div>

        <div className="notification-list">
          {notifications.length === 0 ? (
            <div className="no-notifications">
              <span role="img" aria-label="no notifications" style={{ fontSize: 50, display: "block", marginBottom: 10 }}>📭</span>
              <strong>No new notifications</strong>
              <div style={{ fontSize: 14, color: "#aaa", marginTop: 5 }}>
                You're all caught up!
              </div>
            </div>
          ) : (
            notifications.map((notification, i) => (
              <div
                className="notification-item creative"
                key={notification.id || i}
                onClick={() => handleNotificationClick(notification)}
                tabIndex={0}
                title="View details"
              >
                <div className="icon-container creative-icon">
                  {/* Use a colored circle based on notification type for visual creativity */}
                  <span role="img" aria-label="mail" className="mail-emoji">📩</span>
                </div>
                <div className="notification-content">
                  <strong>{notification.title}</strong>
                  <p>{notification.message?.length > 60 ? notification.message.slice(0, 60) + "..." : notification.message}</p>
                </div>
                <span className="timestamp creative-timestamp">
                  {new Date(notification.timestamp).toLocaleString([], { dateStyle: "short", timeStyle: "short" })}
                </span>
              </div>
            ))
          )}
        </div>

        {selectedNotification && (
          <div className="notification-details-popup creative-details-popup">
            <div className="details-content">
              <h3>
                <span role="img" aria-label="detail" style={{ marginRight: 6 }}>📢</span>
                {selectedNotification.title}
              </h3>
              <p>{selectedNotification.message}</p>
              <div className="details-timestamp">
                <span>
                  <span role="img" aria-label="clock">🕒</span>{" "}
                  {new Date(selectedNotification.timestamp).toLocaleString([], { dateStyle: "long", timeStyle: "medium" })}
                </span>
              </div>
              <button className="close-details-btn" onClick={closeDetailsPopup}>
                Close
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Notification;