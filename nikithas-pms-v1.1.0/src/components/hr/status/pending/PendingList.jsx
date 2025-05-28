import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./PendingList.css";
import "./ResponsivePending.css";
import { FaSearch, FaHome } from "react-icons/fa";
import { MdCallMade } from "react-icons/md";
import logo from "../../../../assets/images/nikithas-logo.png";
import Modal from "../../../modal/Modal";

const teamMembers = [
  {
    name: "Sarah Wilson",
    department: "Product Design",
    position: "Senior Designer",
    self: "Pending",
    manager: "pending",
    image: "https://randomuser.me/api/portraits/women/1.jpg",
  },
  {
    name: "John Doe",
    department: "Engineering",
    position: "Software Engineer",
    self: "Completed",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/1.jpg",
  },
  {
    name: "Alex Johnson",
    department: "Marketing",
    position: "Content Strategist",
    self: "completed",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/2.jpg",
  },
  {
    name: "David Lee",
    department: "Sales",
    position: "Sales Director",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/3.jpg",
  },
  {
    name: "Emily Brown",
    department: "HR",
    position: "HR Manager",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/women/2.jpg",
  },
  {
    name: "Robert Taylor",
    department: "Engineering",
    position: "DevOps Engineer",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/4.jpg",
  },
  {
    name: "Lisa Wang",
    department: "Product Design",
    position: "UI/UX Designer",
    self: "Completed",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/women/3.jpg",
  },
  {
    name: "James Wilson",
    department: "Marketing",
    position: "SEO Specialist",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/5.jpg",
  },
  {
    name: "Maria Garcia",
    department: "Sales",
    position: "Account Manager",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/women/4.jpg",
  },
  {
    name: "Thomas Anderson",
    department: "Engineering",
    position: "Backend Developer",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/6.jpg",
  },
  {
    name: "James Wilson",
    department: "Marketing",
    position: "SEO Specialist",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/5.jpg",
  },
  {
    name: "Maria Garcia",
    department: "Sales",
    position: "Account Manager",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/women/4.jpg",
  },
  {
    name: "Thomas Anderson",
    department: "Engineering",
    position: "Backend Developer",
    self: "Pending",
    manager: "Pending",
    image: "https://randomuser.me/api/portraits/men/6.jpg",
  },
];

export default function PendingList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");

  const navigate = useNavigate();
  const entriesPerPage = 10;

  const filteredTeam = teamMembers.filter(
    (member) =>
      member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.department.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(
    startIndex,
    startIndex + entriesPerPage
  );

  const handlePageChange = (newPage) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  const notifyEmployee = (employeeName) => {
    setErrorMessage(`Notification has been sent to ${employeeName}.`);
    setTitle("Notification");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
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
                <td className="hr-pending-team-member">
                  <img
                    src={member.image}
                    alt={member.name}
                    className="hr-pending-profile-pic"
                  />
                  {member.name}
                </td>
                <td>{member.department}</td>
                <td>{member.position}</td>
                <td
                  style={{
                    color: member.self === "Completed" ? "green" : "orange",
                  }}
                >
                  {member.self}
                </td>
                <td
                  style={{
                    color: member.manager === "Completed" ? "green" : "orange",
                  }}
                >
                  {member.manager}
                </td>
                <td className="hr-pending-notify-icon">
                  <MdCallMade
                    className="notify-bell"
                    onClick={() => notifyEmployee(member.name)}
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
