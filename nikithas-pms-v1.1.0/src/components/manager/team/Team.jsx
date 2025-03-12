import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Team.css";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from '../../../assets/images/nikithas-logo.png';
import { Link } from "react-router-dom";
import Loader from '../../modal/loader/Loader';

const teamMembers = [
  { name: "Sarah Wilson", department: "Product Design", position: "Senior Designer", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  { name: "John Doe", department: "Engineering", position: "Software Engineer", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  { name: "Alicia Martinez", department: "Marketing", position: "Marketing Manager", email: "alicia.m@company.com", image: "https://randomuser.me/api/portraits/women/5.jpg" },
  { name: "Michael Brown", department: "Sales", position: "Regional Sales Manager", email: "michael.b@company.com", image: "https://randomuser.me/api/portraits/men/7.jpg" },
  { name: "Emily Davis", department: "HR", position: "HR Specialist", email: "emily.d@company.com", image: "https://randomuser.me/api/portraits/women/6.jpg" },
  { name: "Chris Johnson", department: "Engineering", position: "Full Stack Developer", email: "chris.j@company.com", image: "https://randomuser.me/api/portraits/men/8.jpg" },
  { name: "Sophia Lee", department: "Product Design", position: "UX Researcher", email: "sophia.l@company.com", image: "https://randomuser.me/api/portraits/women/7.jpg" },
  { name: "Daniel Smith", department: "Engineering", position: "Cloud Engineer", email: "daniel.s@company.com", image: "https://randomuser.me/api/portraits/men/9.jpg" },
  { name: "Olivia Garcia", department: "Marketing", position: "Social Media Manager", email: "olivia.g@company.com", image: "https://randomuser.me/api/portraits/women/8.jpg" },
  { name: "Kevin White", department: "Sales", position: "Account Executive", email: "kevin.w@company.com", image: "https://randomuser.me/api/portraits/men/10.jpg" },
  { name: "Jessica Taylor", department: "HR", position: "Recruitment Specialist", email: "jessica.t@company.com", image: "https://randomuser.me/api/portraits/women/9.jpg" },
  { name: "Matthew Harris", department: "Engineering", position: "Mobile Developer", email: "matthew.h@company.com", image: "https://randomuser.me/api/portraits/men/11.jpg" },
  { name: "Chloe Robinson", department: "Product Design", position: "Visual Designer", email: "chloe.r@company.com", image: "https://randomuser.me/api/portraits/women/10.jpg" },
  { name: "Ryan Martinez", department: "Engineering", position: "Data Scientist", email: "ryan.m@company.com", image: "https://randomuser.me/api/portraits/men/12.jpg" },
  { name: "Emma Lopez", department: "Marketing", position: "Brand Strategist", email: "emma.l@company.com", image: "https://randomuser.me/api/portraits/women/11.jpg" },
];



export default function TeamPage() {
  const [teamList, setTeamList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 6;

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
        if (error.response?.status === 401) {
          localStorage.removeItem("token");
          navigate("/");
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
    <div className="manager-team-container">
      {loading && <Loader />}
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
          <img src={logo} alt="Company Logo" className="manager-team-company-logo" />
        </div>

        <div className="manager-team-table-container">
          <table className="manager-team-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Department</th>
                <th>Position</th>
                <th>Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {currentEntries.map((member, index) => (
                <tr key={index}>
                  <td className="manager-team-member">
                    <img src={member.image} alt={member.name} className="manager-team-profile-pic" />
                    {member.name}
                  </td>
                  <td>{member.department}</td>
                  <td>{member.position}</td>
                  <td>{member.email}</td>
                  <td>
                    <Link to="/manager-review" className="manager-team-start-pms-button">Start PMS</Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="manager-team-pagination">
          <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
            Prev
          </button>
          <span>Page {currentPage} of {totalPages}</span>
          <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>
            Next
          </button>
        </div>
      </div>
    </div>
  );
}
