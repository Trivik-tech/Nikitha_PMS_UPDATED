import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import axios from "axios";
import "./ManagerProfile.css";
import "./ResponsiveManagerProfile.css"; // ✅ Resolved conflict - keeping this file
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from '../../modal/loader/Loader';

const ManagerProfile = () => {
  const [managerData, setManagerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // useEffect(() => {
  //   const fetchData = async () => {
  //     const token = localStorage.getItem("token");

  //     if (!token) {
  //       console.error("No token found. Redirecting to login...");
  //       navigate("/login");
  //       return;
  //     }

  //     try {
  //       const response = await axios.get("http://localhost:8080/api/v1/pms/manager/profile", {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       });
  //       setManagerData(response.data);
  //     } catch (error) {
  //       console.error("Error fetching data:", error);
  //       if (error.response && error.response.status === 401) {
  //         console.error("Unauthorized! Redirecting to login...");
  //         localStorage.removeItem("token");
  //         navigate("/login");
  //       }
  //     } finally {
  //       setLoading(false);
  //     }
  //   };

  //   fetchData();
  // }, [navigate]);

  return (
    <div className="manager-container">
      {/* {loading && <Loader />} */}
      <header className="manager-header">
        <div className="header-left">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="icon home-icon" />
          </Link>
        </div>
        <img src={logo} alt="Logo" className="manager-logo" />
      </header>

      <div className="manager-profile-card fade-in">
        <div className="profile-left">
          <img src={profile} alt="Profile" className="manager-profile-pic scale-in" />
        </div>
        <div className="profile-right">
          <h2>{managerData?.firstName || "Ravindra"} {managerData?.lastName || "Kulkarni"}</h2>
          <p className="designation">Senior Software Engineer</p>
          <p className="location">
            {managerData?.location?.name || "Bengaluru"}, {managerData?.location?.state?.name || "Karnataka"}
          </p>
        </div>
      </div>

      <div className="manager-details fade-in">
        <div className="manager-section">
          <h3>Personal Information</h3>
          <div className="info-list">
            <p><strong>First Name:</strong> {managerData?.firstName || "Ravindra"}</p>
            <p><strong>Last Name:</strong> {managerData?.lastName || "Kulkarni"}</p>
            <p><strong>Gender:</strong> Male</p>
            <p><strong>Blood Group:</strong> A+</p>
          </div>
        </div>

        <div className="manager-section">
          <h3>Professional Details</h3>
          <div className="info-list">
            <p><strong>Department:</strong> {managerData?.department?.name || "Engineering"}</p>
            <p><strong>Office Location:</strong> {managerData?.location?.name || "Bengaluru"}</p>
          </div>
        </div>
      </div>

      <div className="manager-contact-info fade-in">
        <h3>Contact Information</h3>
        <div className="contact-card hover-zoom">
          <p><strong>Email Address:</strong> {managerData?.email || "ravindrakulkarni@gmail.com"}</p>
        </div>
        <div className="contact-card hover-zoom">
          <p><strong>Mobile Number:</strong> {managerData?.contactNumber || "1234567895"}</p>
        </div>
        <div className="contact-card hover-zoom">
          <p><strong>Location:</strong> {managerData?.location?.name || "20, Watch Factory Rd, Phase -1, Yeswanthpur, Bengaluru, Karnataka 560013"}, {managerData?.location?.state?.name || "Karnataka"} {managerData?.location?.zipCode || "12345"}</p>
        </div>
      </div>
    </div>
  );
};

export default ManagerProfile;
