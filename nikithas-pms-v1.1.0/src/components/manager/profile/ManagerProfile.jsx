import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import axios from "axios";
import "./ManagerProfile.css";
import "./ResponsiveProfile.css";
import logo from "../../../assets/images/nikithas-logo.png";
import profile from "../../../assets/images/profile1.jpg";
import Loader from "../../modal/loader/Loader";
import { baseUrl } from "../../urls/CommenUrl";

const ManagerProfile = () => {
  const [managerData, setManagerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");

  useEffect(() => {
    let isMounted = true; // Prevent setting state on unmounted component
    setLoading(true); // Always set loading to true at the start of the effect
    const loadProfile = async () => {
      try {
        const result = await axios.get(
          `${baseUrl}/api/v1/pms/manager/profile`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (isMounted) {
          setManagerData(result.data);
        }
      } catch (error) {
        if (isMounted) setManagerData(null);
        console.log(error.message);
      } finally {
        if (isMounted) setLoading(false);
      }
    };

    loadProfile();
    return () => {
      isMounted = false;
    };
  }, [token]);

  // Define the same number of fields for both sections
  const personalFields = [
    { label: "Name", value: managerData?.name },
    { label: "Date of Birth", value: managerData?.dob },
    { label: "Mobile Number", value: managerData?.mobileNumber },
    { label: "Personal Email", value: managerData?.emailId },
    { label: "Branch", value: managerData?.branch },
    { label: "Manager ID", value: managerData?.managerId },
  ];

  const professionalFields = [
    { label: "Department", value: managerData?.department?.name },
    { label: "Designation", value: managerData?.currentDesignation },
    { label: "Role", value: managerData?.role },
    { label: "Official Email", value: managerData?.officialEmailId },
    { label: "Reporting Manager", value: managerData?.reportingManager },
    { label: "Branch", value: managerData?.branch },
  ];

  return (
    <div className="manager-container">
      {loading ? (
        <Loader />
      ) : (
        <>
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
              <img
                src={profile}
                alt="Profile"
                className="manager-profile-pic scale-in"
              />
            </div>
            <div className="profile-right">
              <h2>{managerData?.name || "N/A"}</h2>
              <p className="designation">{managerData?.currentDesignation || "N/A"}</p>
              <p className="location">
                {managerData?.branch || "N/A"}
              </p>
            </div>
          </div>

          <div className="manager-details fade-in">
            <div className="manager-section">
              <h3>Personal Information</h3>
              <div className="info-list">
                {personalFields.map((field, idx) => (
                  <p key={idx}>
                    <strong>{field.label}:</strong> {field.value || "N/A"}
                  </p>
                ))}
              </div>
            </div>

            <div className="manager-section">
              <h3>Professional Details</h3>
              <div className="info-list">
                {professionalFields.map((field, idx) => (
                  <p key={idx}>
                    <strong>{field.label}:</strong> {field.value || "N/A"}
                  </p>
                ))}
              </div>
            </div>
          </div>

          <div className="manager-contact-info fade-in">
            <h3>Contact Information</h3>
            <div className="contact-card hover-zoom">
              <p>
                <strong>Personal Email:</strong> {managerData?.emailId || "N/A"}
              </p>
            </div>
            <div className="contact-card hover-zoom">
              <p>
                <strong>Mobile Number:</strong> {managerData?.mobileNumber || "N/A"}
              </p>
            </div>
            <div className="contact-card hover-zoom">
              <p>
                <strong>Branch:</strong> {managerData?.branch || "N/A"}
              </p>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default ManagerProfile;