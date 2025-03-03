import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Complete.css";
import { FaSearch } from "react-icons/fa";
import logo from '../../../../assets/images/nikithas-logo.png';

import Loader from '../../../modal/loader/Loader';

const teamMembers = [
  { name: "Sarah Wilson", department: "Product Design", position: "Senior Designer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  { name: "John Doe", department: "Engineering", position: "Software Engineer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  { name: "Alex Johnson", department: "Marketing", position: "Content Strategist", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/2.jpg" },
  { name: "David Lee", department: "Sales", position: "Sales Director", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/3.jpg" },
  { name: "Emily Brown", department: "HR", position: "HR Manager", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/women/2.jpg" },
  { name: "Robert Taylor", department: "Engineering", position: "DevOps Engineer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/4.jpg" },
  { name: "Lisa Wang", department: "Product Design", position: "UI/UX Designer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/women/3.jpg" },
  { name: "James Wilson", department: "Marketing", position: "SEO Specialist", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/5.jpg" },
  { name: "Maria Garcia", department: "Sales", position: "Account Manager", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/women/4.jpg" },
  { name: "Thomas Anderson", department: "Engineering", position: "Backend Developer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/6.jpg" },
  { name: "James Wilson", department: "Marketing", position: "SEO Specialist", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/5.jpg" },
  { name: "Maria Garcia", department: "Sales", position: "Account Manager", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/women/4.jpg" },
  { name: "Thomas Anderson", department: "Engineering", position: "Backend Developer", self: "Completed", manager:"Completed", image: "https://randomuser.me/api/portraits/men/6.jpg" },
];

export default function Complete() {
  const [teamList, setTeamList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 10;  // Show fewer entries to ensure scrollable table

  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        navigate("/login");
        return;
      }

      try {
        const response = await axios.get("http://localhost:8080/api/v1/pms/manager/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTeamList(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
        if (error.response?.status === 401) {
          localStorage.removeItem("token");
          navigate("/login");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

 

  const filteredTeam = teamMembers.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    member.email.toLowerCase().includes(searchTerm.toLowerCase())
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
       {loading && <Loader />}
      {/* Header with Title, Search Bar, and Logo */}
      <div className="complete-header">
        <h1>Assessment Completion List</h1>
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

      {/* Scrollable Table Container */}
      <div className="complete-table-container">
        <table className="complete-team-table" >
          <thead>
            <tr>
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
                <td className="complete-team-member">
                  <img src={member.image} alt={member.name} className="complete-profile-pic" />
                  {member.name}
                </td>
                <td>{member.department}</td>
                <td>{member.position}</td>
                <td style={{ color: "green" }}>{member.self}</td>
                <td style={{ color: "green" }}>{member.manager}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="complete-pagination">
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
