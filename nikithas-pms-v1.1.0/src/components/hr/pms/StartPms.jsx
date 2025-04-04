import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaSearch, FaHome, FaFilter } from "react-icons/fa";
import logo from '../../../assets/images/nikithas-logo.png';

import PmsInitiated from '../../modal/pms/PmsInitiated';
import "./StartPms.css";

const teamMembers = [
  { id: "Emp001", name: "John Doe", department: "Engineering", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  { id: "Emp002", name: "Sarah Wilson", department: "Product Design", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  { id: "Emp003", name: "Alex Johnson", department: "Marketing", email: "alex.j@company.com", image: "https://randomuser.me/api/portraits/men/2.jpg" },
  { id: "Emp004", name: "David Lee", department: "Sales", email: "david.l@company.com", image: "https://randomuser.me/api/portraits/men/3.jpg" },
  { id: "Emp005", name: "Emily Brown", department: "HR", email: "emily.b@company.com", image: "https://randomuser.me/api/portraits/women/2.jpg" },
];

export default function StartPms() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState("");
  const [selectedEmployeeId, setSelectedEmployeeId] = useState("");
  const navigate = useNavigate();
  const entriesPerPage = 10;

  const filteredTeam = teamMembers.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    member.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  return (
    <div className="start-pms-container">
      <div className="start-pms-content">
        <div className="start-pms-header">
          <div className="start-pms-header-title">
            <Link to="/hr-dashboard"> 
              <FaHome className="start-pms-home-icon" />
            </Link>
            <h2>Employee List</h2>
          </div>

          <div className="start-pms-search-filter-container">
            <div className="start-pms-search-bar">
              <FaSearch className="start-pms-search-icon" />
              <input
                type="text"
                placeholder="Search team members..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            {/* <button className="start-pms-filter-button">
              <FaFilter className="start-pms-filter-icon" />
            </button> */}
          </div>
          <img src={logo} alt="Company Logo" className="start-pms-company-logo" />
        </div>

        <div className="start-pms-table-container">
          <table className="start-pms-team-table">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Department</th>
                <th>Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {currentEntries.map((member) => (
                <tr key={member.id}>
                  <td>{member.id}</td>
                  <td className="start-pms-team-member">
                    <img src={member.image} alt={member.name} className="start-pms-profile-pic" />
                    {member.name}
                  </td>
                  <td>{member.department}</td>
                  <td>{member.email}</td>
                  <td>
                    <button
                      className="start-pms-button"
                      onClick={() => {
                        setSelectedEmployee(member.name);
                        setSelectedEmployeeId(member.id);
                        setShowModal(true);
                      }}
                    >
                      Initiate PMS
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="start-pms-pagination">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Prev</button>
          <span> Page {currentPage} of {totalPages} </span>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
        </div>
      </div>
      
      {showModal && <PmsInitiated onClose={() => setShowModal(false)} employeeName={selectedEmployee} employeeId={selectedEmployeeId} />}

    </div>
  );
}
