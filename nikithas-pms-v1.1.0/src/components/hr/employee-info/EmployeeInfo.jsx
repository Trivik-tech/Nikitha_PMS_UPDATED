import React, { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import "./EmployeeInfo.css";
import "./InfoResponsive.css"
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from "../../modal/loader/Loader";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";
import { decrypt } from "../../utils/encryptUtils";

const EmployeeInfo = () => {
  const [employeeData, setEmployeeData] = useState(null);
  const [loading, setLoading] = useState(true);
 const { id: encodedId } = useParams();
  const id = decrypt(encodedId);
  const token=localStorage.getItem('token')

  const formatDateToDDMMYYYY = (dateString) => {
    if (!dateString) return "-";
    const date = new Date(dateString);
    if (isNaN(date)) return "-";
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  useEffect(() => {
    const loadEmployeeData = async () => {
      try {
        const response = await axios.get(
          `${baseUrl}/api/v1/pms/hr/get-employee/${id}`,{
            headers:{
              Authorization:`Bearer ${token}`
            }

          }
        );

        const data = response.data;

        data.dob = formatDateToDDMMYYYY(data.dob);
        data.dateOfJoining = formatDateToDDMMYYYY(data.dateOfJoining);
        data.lastWorkingDate = formatDateToDDMMYYYY(data.lastWorkingDate);

        setEmployeeData(data);
      } catch (error) {
        console.error("Error fetching employee data:", error);
      } finally {
        setLoading(false);
      }
    };

    loadEmployeeData();
  }, [id]);

  if (loading) {
    return <Loader />;
  }

  if (!employeeData) {
    return <div>No employee data available.</div>;
  }

  return (
    <div className="hr-module-employee-info-wrapper">
      <header className="hr-module-employee-info-header">
        <div className="hr-module-header-left">
          <Link to="/hr-dashboard" className="hr-module-home-link">
            <FaHome className="hr-module-home-icon" />
          </Link>
        </div>
        <img src={logo} alt="Logo" className="hr-module-logo" />
      </header>

      <div className="hr-module-employee-info-content">
        <div className="hr-module-profile-card">
          <div className="hr-module-profile-left">
            <img
              src={profile}
              alt="Profile"
              className="hr-module-profile-pic"
            />
          </div>
          <div className="hr-module-profile-right">
            <h2>{employeeData.name || "John Doe"}</h2>
            <div className="hr-module-info-row">
              <p className="hr-module-designation">
                {employeeData.currentDesignation || "Software Engineer"}
              </p>
              <p className="hr-module-location">{employeeData.branch}</p>
            </div>
          </div>
        </div>

        <div className="hr-module-info-sections">
          <div className="hr-module-info-section-container">
            <div className="hr-module-info-section">
              <h3>Personal Information</h3>
              <ul>
                <li>
                  <strong>Employee ID:</strong>{" "}
                  {employeeData.empId || "E1234"}
                </li>
                <li>
                  <strong>Name:</strong> {employeeData.name || "John Doe"}
                </li>
                <li>
                  <strong>Date of Birth:</strong>{" "}
                  {employeeData.dob || "-"}
                </li>
                <li>
                  <strong>Category:</strong>{" "}
                  {employeeData.category || "Full-time"}
                </li>
              </ul>
            </div>

            <div className="hr-module-info-section">
              <h3>Professional Details</h3>
              <ul>
                <li>
                  <strong>Department:</strong>{" "}
                  {employeeData.department || "Engineering"}
                </li>
                <li>
                  <strong>Branch:</strong>{" "}
                  {employeeData.branch || "Headquarters"}
                </li>
                <li>
                  <strong>Designation:</strong>{" "}
                  {employeeData.currentDesignation || "Software Engineer"}
                </li>
                <li>
                  <strong>Reporting Manager:</strong>{" "}
                  {employeeData.reportingManager || "Jane Smith"}
                </li>
                <li>
                  <strong>Date of Joining:</strong>{" "}
                  {employeeData.dateOfJoining || "-"}
                </li>
                <li>
                  <strong>Last Working Date:</strong>{" "}
                  {employeeData.lastWorkingDate || "-"}
                </li>
              </ul>
            </div>
          </div>

          <div className="hr-module-info-section">
            <h3>Contact Information</h3>
            <ul>
              <li>
                <strong>Email Address:</strong>{" "}
                {employeeData.emailId || "johndoe@company.com"}
              </li>
              <li>
                <strong>Official Email ID:</strong>{" "}
                {employeeData.officialEmailId || "john.doe@example.com"}
              </li>
              <li>
                <strong>Mobile Number:</strong>{" "}
                {employeeData.mobileNumber || "9876543210"}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmployeeInfo;
