import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CompletedList.css";
import "./CompletedListResponsive.css";
import { FaSearch, FaHome, FaDownload } from "react-icons/fa";
import logo from '../../../../assets/images/nikithas-logo.png';
import axios from "axios";
import { baseUrl } from '../../../urls/CommenUrl'

export default function CompletedList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [employees, setEmployees] = useState([]);
  const navigate = useNavigate();
  const entriesPerPage = 6;
  const token = localStorage.getItem('token')

  const loadCompletedEmployees = React.useCallback(async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/completed`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setEmployees(result.data);
      console.log(result.data);
    } catch (error) {
      console.error(error);
    }
  }, [token]);

  useEffect(() => {
    loadCompletedEmployees();
  }, [loadCompletedEmployees]);


  useEffect(() => {
    document.body.classList.add("fade-in");
    return () => document.body.classList.remove("fade-in");
  }, []);

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

  const exportPdf = async (empId) => {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/export-pdf/${empId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        responseType: 'blob' // Important for binary file like PDF
      });

      // Create a Blob from the PDF stream
      const blob = new Blob([response.data], { type: 'application/pdf' });

      // Create a temporary download link
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;

      // Set a meaningful filename
      const currentDate = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
      link.download = `Employee_Report_${empId}_${currentDate}.pdf`;

      // Append, click and remove the link
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // Clean up the URL object
      window.URL.revokeObjectURL(url);

      console.log("PDF downloaded successfully");

    } catch (error) {
      console.error("Failed to download PDF:", error.message);
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

            </tr>
          </thead>
          <tbody>
            {currentEntries.map((member, index) => (
              <tr key={index}>
                <td>{member.empId}</td>
                <td className="hr-complete-team-member">{member.name}</td>
                <td>{member.department?.name || "N/A"}</td>
                <td>{member.currentDesignation}</td>
                <td><FaDownload
                  title="download"
                  className="employee-list-edit-icon"
                  onClick={() => exportPdf(member.empId)} /></td>

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
