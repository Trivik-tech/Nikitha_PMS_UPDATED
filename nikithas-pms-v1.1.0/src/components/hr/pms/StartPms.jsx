import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaSearch, FaHome } from "react-icons/fa";
import axios from "axios";
import logo from "../../../assets/images/nikithas-logo.png";
import PmsInitiated from "../../modal/pms/PmsInitiated";
import "./StartPms.css";
import "./ResponsiveStartPMS.css";
import { baseUrl } from "../../urls/CommenUrl";

export default function StartPms() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState("");
  const [selectedEmployeeId, setSelectedEmployeeId] = useState("");
  const [team, setTeam] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");
  const [hasServerError, setHasServerError] = useState(false);

  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const fetchEmployees = async () => {
    try {
      const response = await axios.get(
        `${baseUrl}/api/v1/pms/hr/employee-with-pms-initiated`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const employees = response.data;
      setTeam(employees);
      setHasServerError(false);
    } catch (error) {
      console.error("Error fetching employee data:", error.message);
      setHasServerError(true);
    }
  };

  useEffect(() => {
    fetchEmployees();
    const intervalId = setInterval(() => {
      fetchEmployees();
    }, 2000);
    return () => clearInterval(intervalId);
  }, []);

  const entriesPerPage = 10;

  const filteredTeam = team.filter((member) =>
    (member.name?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (member.email?.toLowerCase() || "").includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const handleSort = () => {
    const sorted = [...team].sort((a, b) => {
      return sortOrder === "asc"
        ? (a.name || "").localeCompare(b.name || "")
        : (b.name || "").localeCompare(a.name || "");
    });
    setTeam(sorted);
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  const searchEmployees = async (e) => {
    const search = e.target.value;
    setSearchTerm(search);

    if (!search.trim()) {
      fetchEmployees();
      return;
    }

    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/all-employees/${search}`);
      setTeam(response.data);
      setHasServerError(false);
    } catch (error) {
      console.error("Search failed:", error.message);
      setHasServerError(true);
    }
  };

  const pmsInitiated = async (id) => {
    try {
      const pms = {
        pms_initiated: true,
      };

      const result = await axios.patch(
        `${baseUrl}/api/v1/pms/hr/pms-initiated/${id}`,
        pms,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log(result.data);
    } catch (error) {
      console.error(error);
    }
  };

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
                placeholder="Search employees..."
                onChange={searchEmployees}
              />
            </div>
          </div>

          <img src={logo} alt="Company Logo" className="start-pms-company-logo" />
        </div>

        <div className="start-pms-table-container">
          <table className="start-pms-team-table">
            <thead>
              <tr>
                <th onClick={handleSort} style={{ cursor: "pointer" }}>Id</th>
                <th>Name</th>
                <th>Department</th>
                <th>Email</th>
                <th>Role</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {currentEntries.map((member) => (
                <tr
                  key={member.id}
                  onClick={() =>
                    navigate(`/employee-info/${member.empId || member.managerId}`)
                  }
                  style={{ cursor: "pointer" }}
                >
                  <td>{member.empId || "-"}</td>
                  <td>{member.name || "-"}</td>
                  <td>{member.department || "-"}</td>
                  <td>{member.officialEmailId || "-"}</td>
                  <td>{member.role || "-"}</td>
                  <td>
                    <button
                      className="start-pms-button"
                      onClick={(e) => {
                        e.stopPropagation();
                        setSelectedEmployee(member.name);
                        setSelectedEmployeeId(member.empId);
                        setShowModal(true);
                        pmsInitiated(member.empId);
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
          <button
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
          >
            Prev
          </button>
          <span>
            Page {currentPage} of {totalPages}
          </span>
          <button
            onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      </div>

      {showModal && (
        <PmsInitiated
          onClose={() => setShowModal(false)}
          employeeName={selectedEmployee}
          employeeId={selectedEmployeeId}
        />
      )}
    </div>
  );
}
