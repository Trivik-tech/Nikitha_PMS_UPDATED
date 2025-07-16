import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "./Complete.css";
import "./ResponsiveManagerCompleted.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from '../../../../assets/images/nikithas-logo.png';
import axios from "axios";
import {baseUrl} from '../../../urls/CommenUrl'

export default function Complete() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [completedTeam, setCompletedTeam] = useState([]);
  const navigate = useNavigate();
  const {id:managerId}=useParams();
  const token =localStorage.getItem("token");
  const entriesPerPage = 6;

  const reportingManagerId = localStorage.getItem("managerId"); // Or hardcoded for testing

  useEffect(() => {
    document.body.classList.add("fade-in");
    return () => document.body.classList.remove("fade-in");
  }, []);

  useEffect(() => {
    fetchCompletedEmployees();
  }, []);

  const fetchCompletedEmployees = async () => {
    try {

      const res = await axios.get(`${baseUrl}/api/v1/pms/manager/completed/${managerId}`,
        {
          headers:{
            Authorization:`Bearer ${token}`
          }
        }
      );
      setCompletedTeam(res.data);
    } catch (err) {
      console.error("Failed to fetch completed employees:", err);
    }
  };

  const filteredTeam = completedTeam.filter((member) =>
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
          <FaHome
            className="complete-home-icon"
            onClick={() => navigate("/manager-dashboard")}
          />
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
              
            </tr>
          </thead>
          <tbody>
            {currentEntries.map((member, index) => (
              <tr key={index}>
                <td className="complete-team-member">{member.name}</td>
                <td>{member.department?.name || "N/A"}</td>
                <td>{member.currentDesignation}</td>
                </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="complete-pagination">
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