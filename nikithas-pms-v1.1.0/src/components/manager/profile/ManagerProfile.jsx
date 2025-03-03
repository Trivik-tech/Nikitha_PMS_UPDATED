import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaArrowLeft, FaHome } from "react-icons/fa";
import axios from "axios";
import "./ManagerProfile.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from '../../modal/loader/Loader';
const ManagerProfile = () => {
  const [managerData, setManagerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("No token found. Redirecting to login...");
        navigate("/login");
        return;
      }

      try {
        const response = await axios.get("http://localhost:8080/api/v1/pms/manager/profile", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setManagerData(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
        if (error.response && error.response.status === 401) {
          console.error("Unauthorized! Redirecting to login...");
          localStorage.removeItem("token");
          navigate("/login");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

  

  return (
    <div className="manager-container">
       {loading && <Loader/>}
      <header className="manager-header">
        <div className="header-left">
          
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="icon home-icon" />
          </Link>
        </div>
        <img src={logo} alt="Logo" className="manager-logo" />
      </header>

      <div className="manager-profile-card">
        <div className="profile-left">
          <img src={profile} alt="Profile" className="manager-profile-pic" />
        </div>
        <div className="profile-right">
          <h2>{managerData?.firstName || "First Name"} {managerData?.lastName || "Last Name"}</h2>
          <p className="designation">Senior Software Engineer</p>
          <p className="location">{managerData?.location.name || "Location"} ,{managerData?.location.state.name || "Location"}</p>
        </div>
      </div>

      <div className="manager-details">
        <div className="manager-section">
          <h3>Personal Information</h3>
          <div className="info-list">
            <p><strong>First Name:</strong> {managerData?.firstName || "N/A"}</p>
            <p><strong>Last Name:</strong> {managerData?.lastName || "N/A"}</p>
            <p><strong>Gender:</strong> Male</p>
            <p><strong>Blood Group:</strong> A+</p>
          </div>
        </div>

        <div className="manager-section">
          <h3>Professional Details</h3>
          <div className="info-list">
            <p><strong>Department:</strong> {managerData?.department.name || "N/A"}</p>
            <p><strong>Office Location:</strong> {managerData?.location.name || "N/A"}</p>
          </div>
        </div>
      </div>

      <div className="manager-contact-info">
        <h3>Contact Information</h3>
        <div className="contact-card">
          <p><strong>Email Address:</strong> {managerData?.email || "N/A"}</p>
        </div>
        <div className="contact-card">
          <p><strong>Mobile Number:</strong> {managerData?.contactNumber || "N/A"}</p>
        </div>
        <div className="contact-card">
          <p><strong>Location:</strong> {managerData?.location.name || "N/A"} ,{managerData?.location.state.name} {managerData?.location.zipCode || "zip code"}</p>
        </div>
      </div>
    </div>
  );
};

export default ManagerProfile;
