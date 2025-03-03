import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaArrowLeft, FaHome } from "react-icons/fa";
import axios from "axios";
import "./HrProfile.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from '../../modal/loader/Loader';

const HrProfile = () => {
  const [managerData, setManagerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  return (
    <div className="hr-container">
       {/* {loading && <Loader/>} */}
      <header className="hr-header">
        <div className="hr-header-left">
          <Link to="/manager-dashboard" className="hr-icon-link">
            <FaHome className="hr-icon hr-home-icon" />
          </Link>
        </div>
        <img src={logo} alt="Logo" className="hr-logo" />
      </header>

      <div className="hr-profile-card">
        <div className="hr-profile-left">
          <img src={profile} alt="Profile" className="hr-profile-pic" />
        </div>
        <div className="hr-profile-right">
          <h2>{managerData?.firstName || "Avinash"} {managerData?.lastName || "V"}</h2>
          <p className="hr-designation">Senior Software Engineer</p>
          <p className="hr-location">{managerData?.location.name || "Peenya"}, {managerData?.location.state.name || "Bangalore"}</p>
        </div>
      </div>

      <div className="hr-details">
        <div className="hr-section">
          <h3>Personal Information</h3>
          <div className="hr-info-list">
            <p><strong>First Name:</strong> {managerData?.firstName || "Guru"}</p>
            <p><strong>Last Name:</strong> {managerData?.lastName || "Nath"}</p>
            <p><strong>Gender:</strong> Male</p>
            <p><strong>Blood Group:</strong> A+</p>
          </div>
        </div>

        <div className="hr-section">
          <h3>Professional Details</h3>
          <div className="hr-info-list">
            <p><strong>Reporting Manager:</strong> {managerData?.department.name || "Guru"}</p>
            <p><strong>Office Location:</strong> {managerData?.location.name || "Bangalore"}</p>
          </div>
        </div>
      </div>

      <div className="hr-contact-info">
        <h3>Contact Information</h3>
        <div className="hr-contact-card">
          <p><strong>Email Address:</strong> {managerData?.email || "Guru@gmail.com"}</p>
        </div>
        <div className="hr-contact-card">
          <p><strong>Mobile Number:</strong> {managerData?.contactNumber || "987654378"}</p>
        </div>
        <div className="hr-contact-card">
          <p><strong>Location:</strong> {managerData?.location.name || "Bangalore, India"}, {managerData?.location.state.name} {managerData?.location.zipCode || "zip code"}</p>
        </div>
      </div>
    </div>
  );
};

export default HrProfile;
