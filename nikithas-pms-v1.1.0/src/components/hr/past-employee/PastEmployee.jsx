import React, { useEffect, useRef, useState } from "react";
import "./PastEmployee.css";
import axios from "axios";
import { FaHome, FaDownload } from "react-icons/fa";
import logo from "../../../assets/images/nikithas-logo.png";
import { useNavigate } from "react-router-dom";
import { baseUrl } from "../../urls/CommenUrl";
import { encrypt } from "../../utils/encryptUtils";
// import Modal from "../../modal/Modal";
// import { Pointer } from "lucide-react"; Settings > Privacy & Security > Developer Mode. 

const PastEmployee = () => {
    const[searchTerm,setSearchTerm]=useState("");
      const [currentPage, setCurrentPage] = useState(1);
      const [team, setTeam] = useState([]);
       const [sortOrder, setSortOrder] = useState("asc");
         const [hasServerError, setHasServerError] = useState(false);
         const [modalOpen, setModalOpen] = useState(false);
           const [successModal, setSuccessModal] = useState(false);
           const [modalMessage, setModalMessage] = useState("");

           const navigate = useNavigate();
           const token = localStorage.getItem("token");

             const hasFetchedRef = useRef(false); // Fix for duplicate API call in StrictMode
         
      const dummyEmployees = [
    { id: 1, empId: "E001", name: "John Doe", department: "IT", officialEmailId: "john.doe@company.com", dateOfJoining: "2021-06-15", role: "Developer" },
    { id: 2, empId: "E002", name: "Jane Smith", department: "HR", officialEmailId: "jane.smith@company.com", dateOfJoining: "2020-04-12", role: "HR Manager" },
    { id: 3, empId: "E003", name: "Michael Johnson", department: "Finance", officialEmailId: "michael.johnson@company.com", dateOfJoining: "2019-08-23", role: "Accountant" },
    { id: 4, empId: "E004", name: "Emily Davis", department: "Marketing", officialEmailId: "emily.davis@company.com", dateOfJoining: "2022-01-10", role: "Marketing Executive" },
    { id: 5, empId: "E005", name: "David Wilson", department: "Operations", officialEmailId: "david.wilson@company.com", dateOfJoining: "2021-11-05", role: "Operations Manager" },
    { id: 6, empId: "E006", name: "Sophia Brown", department: "Sales", officialEmailId: "sophia.brown@company.com", dateOfJoining: "2020-09-19", role: "Sales Executive" },
    { id: 7, empId: "E007", name: "James Miller", department: "Support", officialEmailId: "james.miller@company.com", dateOfJoining: "2021-03-08", role: "Support Engineer" },
    { id: 8, empId: "E008", name: "Olivia Taylor", department: "IT", officialEmailId: "olivia.taylor@company.com", dateOfJoining: "2018-07-25", role: "Senior Developer" },
    { id: 9, empId: "E009", name: "Daniel Anderson", department: "Legal", officialEmailId: "daniel.anderson@company.com", dateOfJoining: "2022-02-14", role: "Legal Advisor" },
    { id: 10, empId: "E010", name: "Ava Martinez", department: "Admin", officialEmailId: "ava.martinez@company.com", dateOfJoining: "2019-12-01", role: "Admin Executive" }
  ];
     

  const fetchEmployees =async()=>{
    try{
        setTeam(dummyEmployees);
        setHasServerError(false);
    }
    catch(error){
        console.error("Error fetching employee data:", error.message);
      setHasServerError(true);
      setTeam(dummyEmployees); // fallback to dummy

    }
  };

  useEffect(() => {
      if (!hasFetchedRef.current) {
        fetchEmployees();
        hasFetchedRef.current = true;
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    
  const entriesPerPage = 10;
  const filteredTeam = team.filter((member) =>
    (member.name?.toLowerCase() || "").includes(searchTerm.toLowerCase()) ||
    (member.emailId?.toLowerCase() || "").includes(searchTerm.toLowerCase())
  );

  const totalPages = Math.ceil(filteredTeam.length / entriesPerPage);
  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = filteredTeam.slice(startIndex, startIndex + entriesPerPage);

  const handleSort = () => {
    const sorted = [...team].sort((a, b) =>
      sortOrder === "asc"
        ? (a.name || "").localeCompare(b.name || "")
        : (b.name || "").localeCompare(a.name || "")
    );

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
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/all-employees/${search}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setTeam(response.data);
      setHasServerError(false);
    } catch (error) {
      console.error("Search failed:", error.message);
      setHasServerError(true);
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
  
//   const handleDownloadData = async (id) => {
//     try {
//       const response = await axios.get(`${baseUrl}/api/v1/pms/hr/generate-report/${id}`, {
//         headers: {
//           Authorization: `Bearer ${token}`,
//         },
//       });

//       if (response.status === 200) {
//         setModalMessage("Employee PDF Report generated and downloaded successfully.");
//         setSuccessModal(true);
//       }
//     } catch (error) {
//       console.error("PDF download failed:", error.message);
//     }
//   };

  const handleExportData= async()=> {
    try {
      const response = await axios.get(`${baseUrl}/api/v1/pms/hr/generate-employee-list`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        setModalMessage("Employee List PDF  generated and downloaded successfully.");
        setSuccessModal(true);
      }
    } catch (error) {
      console.error("PDF download failed:", error.message);
    }
  }

  const isMobile = false;









 
  return (
    <div className="past-employee-fullpage">
      <div className="past-employee-container">
        <div className="past-employee-content">
          <div className="past-employee-header-flex">
            <div className="employee-list-left">
              <FaHome
                className="past-employee-home-icon"
                onClick={() => navigate("/hr-dashboard")}
              />
              <h1>Past employees</h1>
              
            </div>
            <div className="past-employee-center">
              <input 
              type="text" 
              placeholder="Search employees" 
              value={searchTerm}
              onChange={searchEmployees}/>
            </div>
            <img
              src={logo}
              alt="Company Logo"
              className="past-employee-company-logo"
            />
          </div>

          {/* Table Section */}
          <div className="past-employee-table-container">
            <div className="employee-list-table-scroll">
                
              {!isMobile ? (
                <>
                
                
                <table className="past-employee-team-table">
                  <thead>
                    <tr>
                        
                      <th onClick={handleSort} style={{cursor:"pointer"}} >Id{sortOrder==="asc"?"down":"up"}</th>
                      <th>Name</th>
                      <th>Department</th>
                      <th>Email</th>
                      <th>Date of joining</th>
                      <th>Role</th>
                    </tr>
                  </thead>
                  <tbody>
                    {currentEntries.map((member) => (
                      <tr
                         key={member.id}
                                                 onClick={() => {
                                                   const encryptedId = encrypt(member.empId);
                                                   navigate(`/employee-info/${encryptedId || member.managerId}`);
                                                 }}
                                                 style={{ cursor: "pointer" }}
                      >
                        <td>{member.empId || "-"}</td>
                        <td>{member.name || "-"}</td>
                        <td>{member.department || "-"}</td>
                        <td>{member.officialEmailId || "-"}</td>
                        <td>{formatDate(member.dateOfJoining)}</td>
                        <td>{member.role || "-"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                </>
              ) : (
                <div className="employee-list-mobile-cards">{/* Mobile Cards */}</div>
              )}
            </div>
          </div>

          {/* Pagination */}
          {!hasServerError && (
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
              <button title="export" onClick={() => handleExportData()}>
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
