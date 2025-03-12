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
    <div className="team-container fade-in">
      <div className="content slide-up">
        <div className="header slide-down">
          <div className="header-title">
            <Link to="/hr-dashboard"> 
              <FaHome className="home-icon" />
            </Link>
            <h3>Employee List</h3>
          </div>

          <div className="search-filter-container fade-in">
            <div className="search-bar">
              <FaSearch className="search-icon" />
              <input
                type="text"
                placeholder="Search team members..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <button className="filter-button zoom-in">
              <FaFilter className="filter-icon" />
            </button>
          </div>
          <img src={logo} alt="Company Logo" className="company-logo zoom-in" />
        </div>

        <div className="table-container fade-in">
          <table className="team-table">
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
                <tr key={member.id} className="fade-in">
                  <td>{member.id}</td>
                  <td className="team-member">
                    <img src={member.image} alt={member.name} className="profile-pic zoom-in" />
                    {member.name}
                  </td>
                  <td>{member.department}</td>
                  <td>{member.email}</td>
                  <td>
                    <button
                      className="start-pms-button zoom-in"
                      onClick={() => {
                        setSelectedEmployee(member.name);
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

        <div className="pagination fade-in">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Prev</button>
          <span> Page {currentPage} of {totalPages} </span>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
        </div>
      </div>
      
      {showModal && <PmsInitiated onClose={() => setShowModal(false)} employeeName={selectedEmployee} />}
    </div>
  );
}