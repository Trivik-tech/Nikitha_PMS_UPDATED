import React, { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import "./EmployeeInfo.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from "../../modal/loader/Loader"; // Assuming Loader is a component that shows a loading spinner
import axios from "axios";

const EmployeeInfo = () => {
  const [employeeData, setEmployeeData] = useState(null);
  const [loading, setLoading] = useState(true); // Loading state to show loader initially
  const { id } = useParams(); // Get the 'id' from the URL params

  useEffect(() => {
    const loadEmployeeData = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/v1/pms/hr/get-employee/${id}`
        );

        let dob="" +response.data.dob;
        response.data.dob=dob.substring(0,10)
        let doj=""+response.data.dateOfJoining;
        response.data.dateOfJoining=doj.substring(0,10)
        setEmployeeData(response.data);
        // console.log(response.data.response)
      } catch (error) {
        console.error("Error fetching employee data:", error);
      } finally {
        setLoading(false); // Set loading to false after the API call is done
      }
    };

    loadEmployeeData();
  }, [id]); // Re-run the effect whenever the 'id' changes

  if (loading) {
    return <Loader />; // Show the loader while fetching data
  }

  if (!employeeData) {
    return <div>No employee data available.</div>; // Display a message if no data is available
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
                  {employeeData.dob || "1990-01-01"}
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
                  {employeeData.dateOfJoining || "2015-05-20"}
                </li>
                <li>
                  <strong>Last Working Date:</strong>{" "}
                  {employeeData.lastWorkingDate || "00-00-00"}
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
              {/* <li><strong>Location:</strong> {employeeData.location.name}, {employeeData.location.state.name}, {employeeData.location.zipCode || "560001"}</li> */}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmployeeInfo;
