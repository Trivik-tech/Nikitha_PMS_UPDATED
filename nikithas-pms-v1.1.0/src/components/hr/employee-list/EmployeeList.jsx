import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaSearch, FaHome } from "react-icons/fa";
import { FaEdit, FaTrash } from "react-icons/fa";
import logo from '../../../assets/images/nikithas-logo.png';
import './EmployeeList.css';

export default function EmployeeList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [team, setTeam] = useState([
    {id: "Emp001", name: "John Doe", department: "Engineering", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
    {id: "Emp002", name: "Sarah Wilson", department: "Product Design", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
    {id: "Emp003", name: "Alex Johnson", department: "Marketing", email: "alex.j@company.com", image: "https://randomuser.me/api/portraits/men/2.jpg" },
    {id: "Emp004", name: "David Lee", department: "Sales", email: "david.l@company.com", image: "https://randomuser.me/api/portraits/men/3.jpg" },
    {id: "Emp005", name: "Emily Brown", department: "HR", email: "emily.b@company.com", image: "https://randomuser.me/api/portraits/women/2.jpg" },
  ]);

  const navigate = useNavigate();
  const entriesPerPage = 10;

  const filteredTeam = team.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    member.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const [sortOrder, setSortOrder] = useState("asc");

  const handleSort = () => {
    const sortedEmployees = [...team].sort((a, b) => {
      if (sortOrder === "asc") {
        return a.name.localeCompare(b.name);
      } else {
        return b.name.localeCompare(a.name);
      }
    });

    setTeam(sortedEmployees);
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  return (
    <div className="employee-list-container">
      <div className="employee-list-content">
        <div className="employee-list-header">
          <div className="employee-list-header-title">
            <Link to="/hr-dashboard" className="icon-link">
              <FaHome className="employee-list-home-icon" />
            </Link>
            
            <h1 className="hr-module-employee-list-text">Employee List</h1>
            <div className="employee-list-search-bar">
              <FaSearch className="employee-list-search-icon" />
              <input
                type="text"
                placeholder="Search team members..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
          <div className="employee-list-search-filter-container">
            <button className="employee-list-filter-button" onClick={handleSort}>
              <i className="fas fa-filter"></i> Filter
            </button>
          </div>
          <div className="employee-list-add-button-container">
            <Link to="/add-employee" className="employee-list-add-button"> Add Employee</Link> 
          </div>
          <img src={logo} alt="Company Logo" className="employee-list-company-logo" />
        </div>

        <div className="employee-list-table-container">
          <table className="employee-list-team-table">
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
                  <td>{member.name}</td>
                  <td>{member.department}</td>
                  <td>{member.email}</td>
                  <td>
                    <FaEdit className="employee-list-edit-icon" title="Edit" />
                    <FaTrash className="employee-list-delete-icon" title="Delete" />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="employee-list-pagination">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Prev</button>
          <span> Page {currentPage} of {totalPages} </span>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
        </div>
      </div>
    </div>
  );
}
