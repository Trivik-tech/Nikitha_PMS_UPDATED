import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CompletedList.css";
import "./CompletedListResponsive.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from '../../../../assets/images/nikithas-logo.png';
import axios from "axios";

export default function CompletedList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [employees, setEmployees] = useState([]);
  const navigate = useNavigate();
  const entriesPerPage = 6;

  useEffect(() => {
    document.body.classList.add("fade-in");
    return () => document.body.classList.remove("fade-in");
  }, []);

  useEffect(() => {
    loadCompletedEmployees();
  }, []);

  const loadCompletedEmployees = async () => {
    try {
      const result = await axios.get("http://localhost:8080/api/v1/pms/hr/completed");
      setEmployees(result.data);
      console.log(result.data);
    } catch (error) {
      console.error(error);
    }
  };

  const filteredEmployees = employees.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredEmployees.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredEmployees.slice(startIndex, startIndex + entriesPerPage);

  const handlePageChange = (newPage) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  return (
    <div className="hr-complete-team-container">
      <div className="hr-complete-header">
        <div className="hr-complete-header-title">
          <FaHome className="hr-complete-home-icon" onClick={() => navigate('/hr-dashboard')} />
          <h1>Assessment Completion List</h1>
        </div>
        <div className="hr-complete-search-bar">
          <FaSearch className="hr-complete-search-icon" />
          <input
            type="text"
            placeholder="Search team members..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <img src={logo} alt="Company Logo" className="hr-complete-company-logo" />
      </div>

      <div className="hr-complete-table-container">
        <table className="hr-complete-team-table">
          <thead>
            <tr>
              <th>ID</th>
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
                <td>{member.empId}</td>
                <td className="hr-complete-team-member">{member.name}</td>
                <td>{member.department?.name || "N/A"}</td>
                <td>{member.currentDesignation}</td>
                <td style={{ color: member.selfCompleted ? "green" : "red" }}>
                  {member.selfCompleted ? "Completed" : "Pending"}
                </td>
                <td style={{ color: member.managerCompleted ? "green" : "red" }}>
                  {member.managerCompleted ? "Completed" : "Pending"}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="hr-complete-pagination">
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
