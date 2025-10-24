import React, { useEffect, useState } from "react";
import "./AssignKraKpi.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../modal/Modal";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";

const PerformanceReview = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [krakpi, setKraKpi] = useState([]);
  const [selectedKraIds, setSelectedKraIds] = useState([]);
  const [loading, setLoading] = useState(true);

  const { id: employeeId } = useParams();
  const token = localStorage.getItem("token");

  // Handle checkbox selection
  const handleKraCheckboxChange = (kraId) => {    
    setSelectedKraIds((prev) =>
      prev.includes(kraId) ? prev.filter((id) => id !== kraId) : [...prev, kraId]
    );
  };

  // Assign selected KRAs to employee
  const assignKras = async () => {
    if (selectedKraIds.length === 0) {
      setErrorMessage(" select at least one KRA to assign.");
      setTitle("Validation Error");
      setShowModal(true);
      return;
    }

    try {
      const response = await axios.post(
        `${baseUrl}/api/v1/pms/manager/assign/${employeeId}`,
        selectedKraIds, // JSON array of selected KRA IDs
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json", // Important for Spring Boot
          },
        }
      );

      setErrorMessage(response.data.message || "KRA assigned successfully!");
      setTitle("Success");
      setShowModal(true);
    } catch (error) {
      console.error("Error assigning KRA:", error);
      setErrorMessage(
        error.response?.data?.message || "Failed to assign KRA."
      );
      setTitle("Error");
      setShowModal(true);
    }
  };

  // Load KRA/KPI data
  useEffect(() => {
    const loadKraKpi = async () => {
      try {
        const result = await axios.get(
          `${baseUrl}/api/v1/pms/manager/allkrawithkpi`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setKraKpi(result.data);
      } catch (error) {
        console.error("Error fetching KRA/KPI:", error);
      } finally {
        setLoading(false);
      }
    };

    loadKraKpi();
  }, [employeeId, token]);

  if (loading) return <p>Loading KRA and KPI data...</p>;

  return (
    <div className="Assign-KRAKPI-container">
      {showModal && (
        <Modal
          message={errorMessage}
          closeModal={() => setShowModal(false)}
          title={title}
        />
      )}

      <header className="Assign-KRAKPI-header">
        <div className="Assign-KRAKPI-title-section">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="Assign-KRAKPI-icon home-icon" />
          </Link>
          <h1 className="Assign-KRAKPI-title">Assign KRA and KPI</h1>
          <img
            src={logo}
            alt="Company Logo"
            className="Assign-KRAKPI-company-logo"
          />
        </div>
        <div className="Assign-KRAKPI-filters">
          <label>
            Employee Id: <input type="text" value={employeeId} readOnly />
          </label>
        </div>
      </header>

      <div className="Assign-KRAKPI-sections">
        {krakpi.map((kra) => {
          const isSelected = selectedKraIds.includes(kra.kraId);
          return (
            <div key={kra.kraId} className="Assign-KRAKPI-section">
              <div
                className={`Assign-KRAKPI-section-header ${
                  isSelected ? "selected-header" : ""
                }`}
              >
                <input
                  type="checkbox"
                  checked={isSelected}
                  onChange={() => handleKraCheckboxChange(kra.kraId)}
                />
                <h3>KRA - {kra.kraName}</h3>
                <span>Weightage: {kra.weightage}</span>
              </div>

              <table className="Assign-KRAKPI-table">
                <thead>
                  <tr>
                    <th>KPI's</th>
                    <th style={{ width: "5%" }}>Weightage</th>
                  </tr>
                </thead>
                <tbody>
                  {kra.kpi.map((kpi, index) => (
                    <tr key={index}>
                      <td>{kpi.description}</td>
                      <td>{kpi.weightage}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          );
        })}
      </div>

      <div className="Assign-KRAKPI-actions">
        <button
          onClick={assignKras}
          className="Assign-KRAKPI-submit-review-btn"
        >
          Assign Selected KRA(s)
        </button>
      </div>
    </div>
  );
};

export default PerformanceReview;