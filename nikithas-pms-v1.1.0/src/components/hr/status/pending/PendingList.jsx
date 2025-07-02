import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./PendingList.css";
import "./ResponsivePending.css";
import { FaSearch, FaHome } from "react-icons/fa";
import { MdCallMade } from "react-icons/md";
import logo from "../../../../assets/images/nikithas-logo.png";
import Modal from "../../../modal/Modal";

export default function PendingList() {
  const [teamMembers, setTeamMembers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");

  const navigate = useNavigate();
  const entriesPerPage = 10;

  useEffect(() => {
    const fetchPendingEmployees = async () => {
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/pms/hr/pending",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setTeamMembers(response.data);
      } catch (error) {
        console.error("Error fetching pending employees:", error);
      }
    };

    fetchPendingEmployees();
  }, []);

  const filteredTeam = teamMembers.filter(
    (member) =>
      member.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.department?.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const handlePageChange = (newPage) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const notifyEmployee = async (empId, employeeName) => {
    const token = localStorage.getItem("token");

    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/pms/hr/notify/employee-and-manager/${empId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const { message } = response.data;

      setErrorMessage(message || `Notification sent to ${employeeName} and their Manager.`);
      setTitle("Notification");
      setShowModal(true);
    } catch (err) {
      setErrorMessage(`Failed to notify ${employeeName} and their Manager.`);
      setTitle("Error");
      setShowModal(true);
    }
  };

  return (
    <div className="hr-pending-team-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}

      <div className="hr-pending-header">
        <div className="hr-pending-header-title">
          <FaHome
            className="hr-pending-home-icon"
            onClick={() => navigate("/hr-dashboard")}
          />
          <h1>Assessment Pending List</h1>
        </div>
        <div className="hr-pending-search-bar">
          <FaSearch className="hr-pending-search-icon" />
          <input
            type="text"
            placeholder="Search team members..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <img
          src={logo}
          alt="Company Logo"
          className="hr-pending-company-logo"
        />
      </div>

      <div className="hr-pending-table-container">
        <table className="hr-pending-team-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Department</th>
              <th>Position</th>
              <th>Self</th>
              <th>Manager</th>
              <th>Notify</th>
            </tr>
          </thead>
          <tbody>
            {currentEntries.map((member, index) => (
              <tr key={index}>
                <td>{member.name}</td>
                <td>{member.department?.name}</td>
                <td>{member.currentDesignation}</td>
                <td style={{ color: member.selfCompleted ? "green" : "orange" }}>
                  {member.selfCompleted ? "Completed" : "Pending"}
                </td>
                <td style={{ color: member.managerCompleted ? "green" : "orange" }}>
                  {member.managerCompleted ? "Completed" : "Pending"}
                </td>
                <td className="hr-pending-notify-icon">
                  <MdCallMade
                    className="notify-bell"
                    onClick={() => notifyEmployee(member.empId, member.name)}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="hr-pagination-container">
        <button
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        >
          Prev
        </button>
        <span>
          Page {currentPage} of {totalPages}
        </span>
        <button
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
}