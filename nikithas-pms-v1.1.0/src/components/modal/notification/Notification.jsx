import React, { useState } from "react";
import "./Notification.css";

const Notification = ({ onClose }) => {
  const [activeTab, setActiveTab] = useState("unread");
  const [selectedNotification, setSelectedNotification] = useState(null);

  const notifications = [
    { id: 1, title: "System Update", message: "The system will undergo maintenance at 10 PM tonight. Please save your work.", timestamp: "1h ago" },
    { id: 2, title: "New Policy", message: "A new leave policy has been published. Please review it in the portal.", timestamp: "2h ago" },
    { id: 3, title: "Meeting Reminder", message: "Reminder: Project meeting scheduled at 3 PM.", timestamp: "3h ago" },
    { id: 4, title: "System Update", message: "The system will undergo maintenance at 10 PM tonight. Please save your work.", timestamp: "1h ago" },
    { id: 5, title: "New Policy", message: "A new leave policy has been published. Please review it in the portal.", timestamp: "2h ago" },
    { id: 6, title: "Meeting Reminder", message: "Reminder: Project meeting scheduled at 3 PM.", timestamp: "3h ago" }
  ];

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
            Unread <span className="badge">2</span>
          </div>
          <div className={`tab ${activeTab === "read" ? "active-tab" : ""}`} onClick={() => setActiveTab("read")}>
            Read
          </div>
        </div>

        <div className="notification-list">
          {notifications.map((notification) => (
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
          ))}
        </div>

        {/* <div className="modal-footer">
          <button className="mark-all-read">Mark all as read</button>
          <button className="clear-all">Clear all notifications</button>
        </div> */}

        {/* Full Details Popup */}
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
