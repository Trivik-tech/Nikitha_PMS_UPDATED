import React, { useEffect, useState, useRef } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { FaSearch, FaHome, FaEdit, FaDownload, FaTrash } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
import "./EmpResponsive.css";
import "./EmployeeList.css";
import axios from "axios";
import DeleteConfirmation from "../../modal/delete-confirmation/DeleteConfirmation";
import { baseUrl } from "../../urls/CommenUrl";
import { encrypt } from "../../utils/encryptUtils";
import Modal from "../../modal/Modal";
import Loader from "../../modal/loader/Loader";

export default function EmployeeList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [team, setTeam] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");
  const [hasServerError, setHasServerError] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [employeeToUpdate, setEmployeeToUpdate] = useState(null);
  const [employeeName, setEmployeeName] = useState(null);
  const [successModal, setSuccessModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [isExporting, setIsExporting] = useState(false); // ✅ Export-specific loader only

  const navigate = useNavigate();
  const { id: hrid } = useParams();
  const token = localStorage.getItem("token");
  const hasFetchedRef = useRef(false); // Prevent duplicate API call in StrictMode

  const fetchEmployees = async () => {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/all-employees`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setTeam(response.data);
      setHasServerError(false);
    } catch (error) {
      console.error("Error fetching employee data:", error.message);
      setHasServerError(true);
    }
  };

  useEffect(() => {
    if (!hasFetchedRef.current) {
      fetchEmployees();
      hasFetchedRef.current = true;
    }
    console.log(hrid);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 🔍 Debounced search effect
  useEffect(() => {
    const delayDebounce = setTimeout(async () => {
      if (!searchTerm.trim()) {
        fetchEmployees();
        return;
      }
      try {
        const response = await axios.get(`${baseUrl}/api/v1/pms/hr/all-employees/${searchTerm}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTeam(response.data);
        console.log(response.data);
        setHasServerError(false);
      } catch (error) {
        console.error("Search failed:", error.message);
        setHasServerError(true);
      }
    }, 300);

    return () => clearTimeout(delayDebounce);
  }, [searchTerm]);

  const handleSort = () => {
    const sorted = [...team].sort((a, b) =>
      sortOrder === "asc"
        ? (a.name || "").localeCompare(b.name || "")
        : (b.name || "").localeCompare(a.name || "")
    );
    setTeam(sorted);
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  const handleInactiveClick = (id, employeeName) => {
    setEmployeeToUpdate(id);
    setEmployeeName(employeeName);
    setModalOpen(true);
  };

  const inactivateEmployee = async () => {
    if (!employeeToUpdate) return;

    try {
      await axios.put(
        `${baseUrl}/api/v1/pms/hr/inactivate-employee/${employeeToUpdate}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setEmployeeToUpdate(null);
      setModalOpen(false);
      fetchEmployees();
      setModalMessage("Employee marked as inactive successfully.");
      setSuccessModal(true);
    } catch (error) {
      console.error("Error inactivating employee:", error.message);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    if (isNaN(date)) return "-";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleDownloadData = async (id) => {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/generate-report/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200) {
        setModalMessage("Employee PDF Report generated and downloaded successfully.");
        setSuccessModal(true);
      }
    } catch (error) {
      console.error("PDF download failed:", error.message);
    }
  };

  const handleExportData = async () => {
    setIsExporting(true);
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/generate-employee-list`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200) {
        setModalMessage("Employee List PDF generated and downloaded successfully.");
        setSuccessModal(true);
      }
    } catch (error) {
      console.error("PDF download failed:", error.message);
    } finally {
      setIsExporting(false);
    }
  };

  // Pagination
  const entriesPerPage = 10;
  const filteredTeam = team;
  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const isMobile = false; // can enhance later with window size

  return (
    <div className="employee-list-fullpage">
      {isExporting && <Loader />} {/* ✅ Loader shown only when exporting */}

      <div className="employee-list-container">
        <div className="employee-list-content">
          <div className="employee-list-header-flex">
            <div className="employee-list-left">
              <FaHome
                className="employee-list-home-icon"
                onClick={() => navigate("/hr-dashboard")}
              />
              <h1 className="employee-list-title">Employee List</h1>
            </div>

            <div className="employee-list-center">
              <input
                type="text"
                placeholder="Search employees..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
              <div className="employee-list-right">
                <Link to={`/add-employee/${hrid}`} className="employee-list-add-button">
                  Add Employee
                </Link>
              </div>
            </div>

            <img src={logo} alt="Company Logo" className="employee-list-company-logo" />
          </div>

          {hasServerError && (
            <div className="employee-list-error-message">
              Failed to fetch data from server. Please try again later.
            </div>
          )}

          <div className="employee-list-table-container">
            <div className="employee-list-table-scroll">
              {!isMobile ? (
                <table className="employee-list-team-table">
                  <thead>
                    <tr>
                      <th onClick={handleSort} style={{ cursor: "pointer" }}>
                        ID {sortOrder === "asc" ? "|" : "||"}
                      </th>
                      <th>Name</th>
                      <th>Department</th>
                      <th>Email</th>
                      <th>Date of Joining</th>
                      <th>Role</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {currentEntries.map((member) => (
                      <tr
                        key={member.id}
                        onClick={() => {
                          const encryptedId = encrypt(member.empId);
                          navigate(`/employee-info/${encryptedId}`);
                        }}
                        style={{ cursor: "pointer" }}
                      >
                        <td>{member.empId || "-"}</td>
                        <td>{member.name || "-"}</td>
                        <td>{member.department || "-"}</td>
                        <td>{member.officialEmailId || "-"}</td>
                        <td>{formatDate(member.dateOfJoining)}</td>
                        <td>{member.role || "-"}</td>
                        <td onClick={(e) => e.stopPropagation()}>
                          <FaEdit
                            className="employee-list-edit-icon"
                            title="Edit"
                            onClick={() => {
                              const encryptedId = encrypt(member.empId);
                              navigate(`/update-employee/${encryptedId}`);
                            }}
                          />
                          <button
                            className="employee-list-inactive-button"
                            onClick={() => handleInactiveClick(member.empId, member.name)}
                          >
                            Inactive
                          </button>
                          <FaDownload
                            className="employee-list-edit-icon"
                            title="Download"
                            onClick={() => handleDownloadData(member.empId)}
                          />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="employee-list-mobile-cards">
                  {/* TODO: Add mobile card layout */}
                </div>
              )}
            </div>
          </div>

          {!hasServerError && (
            <div className="employee-list-pagination">
              <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>
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
              <button title="export" onClick={handleExportData}>
                Export <FaDownload />
              </button>
            </div>
          )}
        </div>

        <DeleteConfirmation
          isOpen={modalOpen}
          onClose={() => {
            setModalOpen(false);
            setEmployeeToUpdate(null);
          }}
          onConfirm={inactivateEmployee}
          name={employeeName}
          id={employeeToUpdate}
        />

        {successModal && (
          <Modal
            title="Success"
            message={modalMessage}
            closeModal={() => setSuccessModal(false)}
          />
        )}
      </div>
    </div>
  );
}
