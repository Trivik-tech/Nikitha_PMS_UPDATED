import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaSearch, FaHome, FaEdit, FaTrash } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
import "./EmployeeList.css";
import axios from "axios";
import DeleteConfirmation from "../../modal/delete-confirmation/DeleteConfirmation"

export default function EmployeeList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [team, setTeam] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");
  const [hasServerError, setHasServerError] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [employeeToDelete, setEmployeeToDelete] = useState(null);
  const [employeeName, setEmployeeName] = useState(null);
  const navigate = useNavigate();

  const fetchEmployees = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/pms/hr/all-employees");
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
  }, []);

  const entriesPerPage = 10;
  const filteredTeam = team.filter(member =>
    (member.name?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (member.emailId?.toLowerCase() || "").includes(searchTerm.toLowerCase())
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
    if (!search.trim()) {
      fetchEmployees();
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/api/v1/pms/hr/all-employees/${search}`);
      const employees = response.data;
      setTeam(employees);
      setHasServerError(false);
    } catch (error) {
      console.error("Search failed:", error.message);
      setHasServerError(true);
    }
  };

  const handleDeleteClick = (id,employeeName) => {
    setEmployeeToDelete(id);
    setEmployeeName(employeeName)
    
    setModalOpen(true);
  };

  const deleteEmployee = async () => {
    if (!employeeToDelete) return;

    try {
      console.log(employeeToDelete)
      await axios.delete(`http://localhost:8080/api/v1/pms/hr/delete-employee/${employeeToDelete}`);
      console.log(`Employee with ID ${employeeToDelete} deleted`);
      setEmployeeToDelete(null);
      setModalOpen(false);
      fetchEmployees();
    } catch (error) {
      console.error("Error deleting employee:", error.message);
    }
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
                placeholder="Search employees..."
                onChange={(e) => searchEmployees(e)}
              />
            </div>
          </div>

          <div className="employee-list-add-button-container">
            <Link to="/add-employee" className="employee-list-add-button">
              Add Employee
            </Link>
          </div>

          <img src={logo} alt="Company Logo" className="employee-list-company-logo" />
        </div>

        {hasServerError && (
          <div className="employee-list-error-message">
            {/* Show error if needed */}
          </div>
        )}

        <div className="employee-list-table-container">
          <table className="employee-list-team-table">
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
                  onClick={() => navigate(`/employee-info/${member.empId || member.managerId}`)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{member.empId || "-"}</td>
                  <td>{member.name || "-"}</td>
                  <td>{member.department || "-"}</td>
                  <td>{member.officialEmailId || "-"}</td>
                  <td>{member.role || "-"}</td>
                  <td onClick={(e) => e.stopPropagation()}>
                    <FaEdit className="employee-list-edit-icon" title="Edit" />
                    <FaTrash
                      className="employee-list-delete-icon"
                      title="Delete"
                      onClick={() => handleDeleteClick(member.empId,member.name)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {!hasServerError && (
          <div className="employee-list-pagination">
            <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Prev</button>
            <span>Page {currentPage} of {totalPages}</span>
            <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
          </div>
        )}
      </div>

      {/* Modal */}
      <DeleteConfirmation
        isOpen={modalOpen}
        onClose={() => {
          setModalOpen(false);
          setEmployeeToDelete(null);
        }}
        onConfirm={deleteEmployee}
        name={employeeName}
        id={employeeToDelete}
      />
    </div>
  );
}