import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Team.css";
import "./ResponsiveTeam.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
import { Link } from "react-router-dom";
import Loader from "../../modal/loader/Loader";

export default function TeamPage() {
  const [teamList, setTeamList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 6;

  // useEffect(() => {
  //   const fetchData = async () => {
  //     const token = localStorage.getItem("token");
  //     if (!token) {
  //       navigate("/login");
  //       return;
  //     }
  //     try {
  //       const response = await axios.get("http://localhost:8080/api/v1/pms/manager/profile", {
  //         headers: { Authorization: `Bearer ${token}` },
  //       });
  //       setTeamList(response.data);
  //     } catch (error) {
  //       if (error.response?.status === 401) {
  //         localStorage.removeItem("token");
  //         navigate("/");
  //       }
  //     } finally {
  //       setLoading(false);
  //     }
  //   };

  //   fetchData();
  // }, [navigate]);

  useEffect(() => {
    const loadTeam = async () => {
      try {
        const result = await axios.get(
          "http://localhost:8080/api/v1/pms/manager/employee-list/Pradeep Prahalada Rao Kubair"
        );
        console.log(result.data);
        setTeamList(result.data);
      } catch (error) {
        console.error(error);
      }
    };

    loadTeam();
  }, []);

  const filteredTeam = teamList.filter(
    (member) =>
      member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
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
              {currentEntries.map((member) => (
                <tr
                  key={member.id}
                  onClick={() => navigate(`/approve-pms/${member.empId}`)}
                  style={{ cursor: "pointer" }}
                >
                  <td>{member.empId || "-"}</td>
                  <td>{member.name || "-"}</td>
                  <td>{member.department.name || "-"}</td>
                  <td>{member.currentDesignation}</td>
                  <td>{member.officialEmailId || "-"}</td>
                  <td onClick={(e) => e.stopPropagation()}>
                    <Link to="/manager-review" className="manager-team-start-pms-button">
                      Start PMS
                    </Link>
                  </td>
                </tr>
              ))}
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
