import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaArrowLeft, FaHome } from "react-icons/fa";
import axios from "axios";
import "./HrProfile.css";
import "./HrResponsive.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profileImage from "../../../assets/images/profile1.jpg";
import Loader from '../../modal/loader/Loader';
import { baseUrl } from '../../urls/CommenUrl';

const HrProfile = () => {
  const [hrData, setHrData] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const loadProfile = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/profile`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setHrData(result.data.profile);
    } catch (error) {
      console.log(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  if (loading) return <Loader />;

  return (
    <div className="hr-container fade-in">
      <header className="hr-header slide-down">
        <div className="hr-header-left">
          <Link to="/hr-dashboard" className="hr-icon-link">
            <FaHome className="hr-icon hr-home-icon scale-hover" />
          </Link>
        </div>
        <img src={logo} alt="Logo" className="hr-logo" />
      </header>

      <div className="hr-profile-card scale-in">
        <div className="hr-profile-left">
          <img src={profileImage} alt="Profile" className="hr-profile-pic" />
        </div>
        <div className="hr-profile-right">
          <h2>{hrData?.name || "N/A"}</h2>
          <p className="hr-designation">{hrData?.currentDesignation || "N/A"}</p>
          <p className="hr-location">{hrData?.branch || "N/A"}, India</p>
        </div>
      </div>

      <div className="hr-details">
        <div className="hr-section slide-left">
          <h3>Personal Information</h3>
          <div className="hr-info-list">
            <p><strong>Name:</strong> {hrData?.name || "N/A"}</p>
            <p><strong>Date of Birth:</strong> {hrData?.dob || "N/A"}</p>
            <p><strong>Email ID:</strong> {hrData?.emailId || "N/A"}</p>
            <p><strong>Official Email:</strong> {hrData?.officialEmailId || "N/A"}</p>
            <p><strong>Mobile Number:</strong> {hrData?.mobileNumber || "N/A"}</p>
          </div>
        </div>

        <div className="hr-section slide-right">
          <h3>Professional Details</h3>
          <div className="hr-info-list">
            <p><strong>HR ID:</strong> {hrData?.hrId || "N/A"}</p>
            <p><strong>Designation:</strong> {hrData?.currentDesignation || "N/A"}</p>
            <p><strong>Department:</strong> {hrData?.department || "N/A"}</p>
            <p><strong>Grade:</strong> {hrData?.category || "N/A"}</p>
            <p><strong>Reporting Manager:</strong> {hrData?.reportingManager || "N/A"}</p>
            <p><strong>Branch:</strong> {hrData?.branch || "N/A"}</p>
            <p><strong>Date of Joining:</strong> {hrData?.dateOfJoining || "N/A"}</p>
            <p><strong>Last Working Day:</strong> {hrData?.lastWorkingDay || "Currently Working"}</p>
          </div>
        </div>
      </div>

      <div className="hr-contact-info">
        <h3>Contact Information</h3>
        <div className="hr-contact-card glow-hover">
          <p><strong>Email:</strong> {hrData?.emailId || "N/A"}</p>
        </div>
        <div className="hr-contact-card glow-hover">
          <p><strong>Mobile:</strong> {hrData?.mobileNumber || "N/A"}</p>
        </div>
        <div className="hr-contact-card glow-hover">
          <p><strong>Branch:</strong> {hrData?.branch || "N/A"}, India</p>
        </div>
      </div>
    </div>
  );
};

export default HrProfile;
