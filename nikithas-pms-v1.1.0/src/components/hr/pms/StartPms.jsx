import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaSearch, FaHome } from "react-icons/fa";
import logo from '../../../assets/images/nikithas-logo.png';
import { Link } from "react-router-dom";
import "./StartPms.css";

const teamMembers = [
  {id: "Emp001", name: "John Doe", department: "Engineering", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  {id: "Emp002", name: "Sarah Wilson", department: "Product Design", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  {id: "Emp003", name: "Alex Johnson", department: "Marketing", email: "alex.j@company.com", image: "https://randomuser.me/api/portraits/men/2.jpg" },
  {id: "Emp004", name: "David Lee", department: "Sales", email: "david.l@company.com", image: "https://randomuser.me/api/portraits/men/3.jpg" },
  {id: "Emp005", name: "Emily Brown", department: "HR", email: "emily.b@company.com", image: "https://randomuser.me/api/portraits/women/2.jpg" },
  {id: "Emp006", name: "John Doe", department: "Engineering", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  {id: "Emp007", name: "Sarah Wilson", department: "Product Design", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  {id: "Emp008", name: "Alex Johnson", department: "Marketing", email: "alex.j@company.com", image: "https://randomuser.me/api/portraits/men/2.jpg" },
  {id: "Emp009", name: "David Lee", department: "Sales", email: "david.l@company.com", image: "https://randomuser.me/api/portraits/men/3.jpg" },
  {id: "Emp0010", name: "Emily Brown", department: "HR", email: "emily.b@company.com", image: "https://randomuser.me/api/portraits/women/2.jpg" },
  {id: "Emp0011", name: "John Doe", department: "Engineering", email: "john.d@company.com", image: "https://randomuser.me/api/portraits/men/1.jpg" },
  {id: "Emp0012", name: "Sarah Wilson", department: "Product Design", email: "sarah.w@company.com", image: "https://randomuser.me/api/portraits/women/1.jpg" },
  {id: "Emp0013", name: "Alex Johnson", department: "Marketing", email: "alex.j@company.com", image: "https://randomuser.me/api/portraits/men/2.jpg" },
  {id: "Emp0014", name: "David Lee", department: "Sales", email: "david.l@company.com", image: "https://randomuser.me/api/portraits/men/3.jpg" },
  {id: "Emp0015", name: "Emily Brown", department: "HR", email: "emily.b@company.com", image: "https://randomuser.me/api/portraits/women/2.jpg" },
];

export default function StartPms() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const entriesPerPage = 10;

  const filteredTeam = teamMembers.filter(member =>
    member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    member.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  return (
    <div className="team-container">
      <div className="content">
        <div className="header">
          <div className="header-title">
            <FaHome className="home-icon" />
            <h1>Employee List</h1>
          </div>
          <div className="search-bar">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search team members..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <img src={logo} alt="Company Logo" className="company-logo" />
        </div>

        <div className="table-container">
          <table className="team-table">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Department</th>
                <th>Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {currentEntries.map((member) => (
                <tr key={member.id}>
                  <td>{member.id}</td>
                  <td className="team-member">{member.name}</td>
                  <td>{member.department}</td>
                  <td>{member.email}</td>
                  <td>
                    <Link to="/manager-review" className="start-pms-button">Initiate PMS</Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="pagination">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Prev</button>
          <span> Page {currentPage} of {totalPages} </span>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
        </div>
      </div>
    </div>
  );
}
