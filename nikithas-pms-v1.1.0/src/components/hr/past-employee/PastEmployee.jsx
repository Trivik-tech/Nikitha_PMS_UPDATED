import React, { useEffect, useRef, useState } from "react";
import "./PastEmployee.css";
import axios from "axios";
import { FaHome, FaDownload } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
import { useNavigate } from "react-router-dom";
import { baseUrl } from "../../urls/CommenUrl";
import { encrypt } from "../../utils/encryptUtils";

const PastEmployee = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [team, setTeam] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");
  const [hasServerError, setHasServerError] = useState(false);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const hasFetchedRef = useRef(false); // Prevent duplicate API call in StrictMode

  const entriesPerPage = 10;

  // Fetch past employees from backend
  const fetchEmployees = async () => {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/exit-employees`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setTeam(response.data);
      setHasServerError(false);
    } catch (error) {
      console.error("Error fetching past employees:", error.message);
      setHasServerError(true);
      setTeam([]); // fallback empty
    }
  };

  useEffect(() => {
    if (!hasFetchedRef.current) {
      fetchEmployees();
      hasFetchedRef.current = true;
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Search filter
  const filteredTeam = team.filter((member) =>
    (member.name?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (member.officialEmailId?.toLowerCase() || "").includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  // Sorting by Name
  const handleSort = () => {
    const sorted = [...team].sort((a, b) =>
      sortOrder === "asc"
        ? (a.name || "").localeCompare(b.name || "")
        : (b.name || "").localeCompare(a.name || "")
    );
    setTeam(sorted);
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  // Handle search input
  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1); // reset page on search
  };

  // Format date to dd-MM-yyyy
  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    if (isNaN(date)) return "-";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  // Export past employees PDF
  const handleExportData = async () => {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/generate-employee-list`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200) {
        alert("Employee List PDF generated successfully.");
      }
    } catch (error) {
      console.error("PDF export failed:", error.message);
      alert("Failed to generate PDF.");
    }
  };

  return (
    <div className="past-employee-fullpage">
      <div className="past-employee-container">
        <div className="past-employee-content">
          {/* Header */}
          <div className="past-employee-header-flex">
            <div className="employee-list-left">
              <FaHome
                className="past-employee-home-icon"
                onClick={() => navigate("/hr-dashboard")}
              />
              <h1>Past Employees</h1>
            </div>
            <div className="past-employee-center">
              <input
                type="text"
                placeholder="Search employees"
                value={searchTerm}
                onChange={handleSearch}
              />
            </div>
            <img src={logo} alt="Company Logo" className="past-employee-company-logo" />
          </div>

          {/* Table */}
          <div className="past-employee-table-container">
            <div className="employee-list-table-scroll">
              {hasServerError ? (
                <div className="no-data">Failed to load past employees.</div>
              ) : filteredTeam.length === 0 ? (
                <div className="no-data">No past employees found.</div>
              ) : (
                <table className="past-employee-team-table">
                  <thead>
                    <tr>
                      <th onClick={handleSort} style={{ cursor: "pointer" }}>
                        Id {sortOrder === "asc" ? "↑" : "↓"}
                      </th>
                      <th>Name</th>
                      <th>Department</th>
                      <th>Email</th>
                      <th>Date of Joining</th>
                      <th>Role</th>
                    </tr>
                  </thead>
                  <tbody>
                    {currentEntries.map((member) => (
                      <tr
                        key={member.empId}
                        onClick={() => {
                          const encryptedId = encrypt(member.empId);
                          navigate(`/employee-info/${encryptedId}`);
                        }}
                        style={{ cursor: "pointer" }}
                      >
                        <td>{member.empId || "-"}</td>
                        <td>{member.name || "-"}</td>
                        <td>{member.departmentName || "-"}</td>
                        <td>{member.officialEmailId || member.emailId || "-"}</td>
                        <td>{formatDate(member.dateOfJoining)}</td>
                        <td>{member.currentDesignation || "-"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>

          {/* Pagination & Export */}
          {!hasServerError && filteredTeam.length > 0 && (
            <div className="employee-list-pagination">
              <button
                onClick={() => setCurrentPage(currentPage - 1)}
                disabled={currentPage === 1}
              >
                Prev
              </button>
              <span>
                Page {currentPage} of {totalPages}
              </span>
              <button
                onClick={() => setCurrentPage(currentPage + 1)}
                disabled={currentPage === totalPages}
              >
                Next
              </button>
              <button title="Export" onClick={handleExportData}>
                Export <FaDownload />
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PastEmployee;
