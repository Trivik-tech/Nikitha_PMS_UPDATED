import React, { useEffect, useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import axios from "axios";
import "./Pending.css";
import "./ResponsivePending.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from "../../../../assets/images/nikithas-logo.png";
import { MdNotificationsActive } from "react-icons/md";
import Modal from "../../../modal/Modal";

export default function Pending() {
  const [teamList, setTeamList] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [errorMessage, setErrorMessage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const { id: managerId } = useParams();
  const token = localStorage.getItem("token");
  const entriesPerPage = 10;

  useEffect(() => {
    fetchPendingEmployees();
  }, []);

  const fetchPendingEmployees = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/v1/pms/manager/pending/${managerId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setTeamList(res.data);
    } catch (err) {
      console.error("Error fetching pending employees:", err);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const notifyEmployee = async (empId, employeeName) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/pms/manager/notify/employee/${empId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      const { message } = response.data;

      setErrorMessage(message || `Notification has been sent to ${employeeName}.`);
      setTitle("Notification");
      setShowModal(true);
    } catch (err) {
      setErrorMessage(`Failed to notify ${employeeName}.`);
      setTitle("Error");
      setShowModal(true);
    }
  };

  const filteredTeam = teamList.filter(
    (member) =>
      member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.officialEmailId?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const handlePageChange = (newPage) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  return (
    <div className="pending-team-container">
      {showModal && <Modal message={errorMessage} closeModal={closeModal} title={title} />}

      <div className="pending-header">
        <div className="pending-header-title">
          <FaHome className="pending-home-icon" onClick={() => navigate("/manager-dashboard")} />
          <h1>Assessment Pending List</h1>
        </div>
        <div className="pending-search-bar">
          <FaSearch className="pending-search-icon" />
          <input
            type="text"
            placeholder="Search team members..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <img src={logo} alt="Company Logo" className="pending-company-logo" />
      </div>

      <div className="pending-table-container">
        <table className="pending-team-table">
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
                <td className="pending-team-member">{member.name}</td>
                <td>{member.department?.name || "N/A"}</td>
                <td>{member.currentDesignation}</td>
                <td style={{ color: member.selfCompleted ? "green" : "orange" }}>
                  {member.selfCompleted ? "Completed" : "Pending"}
                </td>
                <td style={{ color: member.managerCompleted ? "green" : "orange" }}>
                  {member.managerCompleted ? "Completed" : "Pending"}
                </td>
                <td className="pending-notify-icon">
                  <MdNotificationsActive
                    className={`notify-bell ${member.selfCompleted ? "disabled-bell" : ""}`}
                    onClick={() => {
                      if (!member.selfCompleted) {
                        notifyEmployee(member.empId, member.name);
                      }
                    }}
                    style={{
                      cursor: member.selfCompleted ? "not-allowed" : "pointer",
                      opacity: member.selfCompleted ? 0.4 : 1
                    }}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="pending-pagination">
        <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
          Prev
        </button>
        <span> Page {currentPage} of {totalPages} </span>
        <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>
          Next
        </button>
      </div>
    </div>
  );
}