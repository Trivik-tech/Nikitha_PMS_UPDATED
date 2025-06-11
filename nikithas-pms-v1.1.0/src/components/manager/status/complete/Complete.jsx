import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Complete.css";
import './ResponsiveManagerCompleted.css'; 
import { FaSearch, FaHome } from "react-icons/fa";
import logo from '../../../../assets/images/nikithas-logo.png';

const teamMembers = [
  { name: "Sarah Wilson", department: "Product Design", position: "Senior Designer", self: "Completed", manager: "Completed" },
  { name: "John Doe", department: "Engineering", position: "Software Engineer", self: "Completed", manager: "Completed" },
  { name: "Alex Johnson", department: "Marketing", position: "Content Strategist", self: "Completed", manager: "Completed" },
  { name: "David Lee", department: "Sales", position: "Sales Director", self: "Completed", manager: "Completed"},
];

export default function Complete() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 6;

  useEffect(() => {
    document.body.classList.add("fade-in");
    return () => document.body.classList.remove("fade-in");
  }, []);

  const filteredTeam = teamMembers.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase())
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
    <div className="complete-team-container">
      <div className="complete-header">
        <div className="complete-header-title">
          <FaHome className="complete-home-icon" onClick={() => navigate('/manager-dashboard')} />
          <h1>Assessment Completion List</h1>
        </div>
        <div className="complete-search-bar">
          <FaSearch className="complete-search-icon" />
          <input
            type="text"
            placeholder="Search team members..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <img src={logo} alt="Company Logo" className="complete-company-logo" />
      </div>

      <div className="complete-table-container">
        <table className="complete-team-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Department</th>
              <th>Position</th>
              <th>Self</th>
              <th>Manager</th>
            </tr>
          </thead>
          <tbody>
            {currentEntries.map((member, index) => (
              <tr key={index}>
                <td className="complete-team-member">
                  {/* <img src={member.image} alt={member.name} className="complete-profile-pic" /> */}
                  {member.name}
                </td>
                <td>{member.department}</td>
                <td>{member.position}</td>
                <td style={{ color: "green" }}>{member.self}</td>
                <td style={{ color: "green" }}>{member.manager}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="complete-pagination">
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
