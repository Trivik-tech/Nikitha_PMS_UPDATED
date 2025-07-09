import React, { useEffect, useRef, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";
import axios from "axios";
import "./Team.css";
import "./ResponsiveTeam.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
// import Loader from "../../modal/loader/Loader";
import { baseUrl } from '../../urls/CommenUrl';

export default function TeamPage() {
  const [teamList, setTeamList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 6;
  const intervalRef = useRef(null);
  const { id: managerId } = useParams();

  // Only poll if on desktop/laptop, not on mobile (to avoid issues with mobile browser timers)
  const isMobile = () =>
    /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
      navigator.userAgent
    );

  const loadTeam = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token || !managerId) {
        setTeamList([]);
        setLoading(false);
        return;
      }
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/manager/employee-list/${managerId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(result.data)
      setTeamList(Array.isArray(result.data) ? result.data : []);
    } catch (error) {
      setTeamList([]);
      console.error("Error fetching team list:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setLoading(true);
    loadTeam();

    if (!isMobile()) {
      intervalRef.current = setInterval(loadTeam, 1000);
    }

    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
    // eslint-disable-next-line
  }, [managerId]);

  const filteredTeam = teamList.filter(
    (member) =>
      member.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.officialEmailId?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.max(1, Math.ceil(filteredTeam.length / entriesPerPage));
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(
    startIndex,
    startIndex + entriesPerPage
  );

  const handlePageChange = (newPage) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  // The button should ONLY be enabled if:
  // - selfCompleted === true
  // - pmsInitiated === true
  // - managerCompleted === null OR managerCompleted === false
  // The button should be DISABLED if:
  // - selfCompleted !== true OR
  // - pmsInitiated !== true OR
  // - managerCompleted === true

  const isPmsEnabled = (member) => {
    return (
      member.selfCompleted === true &&
      member.pmsInitiated === true &&
      (member.managerCompleted === null || member.managerCompleted === false)
    );
  };

  return (
    <div className="manager-team-container">
      {/* {loading && <Loader />} */}
      <div className="manager-team-content">
        <div className="manager-team-header">
          <div className="manager-team-header-left">
            <Link to="/manager-dashboard" className="manager-team-home-icon">
              <FaHome />
            </Link>
            <h1 className="manager-team-h1">My Team</h1>
          </div>
          <div className="manager-team-search-bar">
            <FaSearch className="manager-team-search-icon" />
            <input
              type="text"
              placeholder="Search team members..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <img
            src={logo}
            alt="Company Logo"
            className="manager-team-company-logo"
          />
        </div>

        <div className="manager-team-table-container">
          <table className="manager-team-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Department</th>
                <th>Position</th>
                <th>Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {currentEntries.length > 0 ? (
                currentEntries.map((member) => {
                  const pmsEnabled = isPmsEnabled(member);

                  return (
                    <tr
                      key={member.empId}
                      onClick={() => navigate(`/approve-pms/${member.empId}`)}
                      style={{ cursor: "pointer" }}
                    >
                      <td>{member.empId || "-"}</td>
                      <td>{member.name || "-"}</td>
                      <td>{member.department?.name || "-"}</td>
                      <td>{member.currentDesignation || "-"}</td>
                      <td>{member.officialEmailId || "-"}</td>
                      <td onClick={(e) => e.stopPropagation()}>
                        <Link
                          to={
                            pmsEnabled
                              ? `/manager-review/${member.empId}/${managerId}`
                              : "#"
                          }
                          className={`manager-team-start-pms-button ${
                            pmsEnabled ? "" : "disabled"
                          }`}
                          onClick={(e) => {
                            if (!pmsEnabled) e.preventDefault();
                          }}
                          tabIndex={pmsEnabled ? 0 : -1}
                          aria-disabled={!pmsEnabled}
                          style={{
                            pointerEvents: pmsEnabled ? "auto" : "none",
                            opacity: pmsEnabled ? 1 : 0.5,
                          }}
                        >
                          Start PMS
                        </Link>
                      </td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="6" style={{ textAlign: "center" }}>
                    {loading ? "Loading team..." : "No team members found."}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <div className="manager-team-pagination">
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
    </div>
  );
}