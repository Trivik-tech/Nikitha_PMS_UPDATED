import React, { useState, useEffect } from "react";
import "./PerformanceReview.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../modal/Modal";
import axios from "axios";
import { baseUrl } from '../../urls/CommenUrl';
import { BsChatText } from "react-icons/bs";

const PerformanceReview = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [krakpi, setKraKpi] = useState([]);
  const [employeeName, setEmployeeName] = useState("");
  const [designation, setDesignation] = useState("");
  const [department, setDepartment] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [employeeReviewDate, setEmployeeReviewDate] = useState("");
  const [managerReviewDate, setManagerReviewDate] = useState("");
  const [remarks, setRemarks] = useState(""); // ✅ Overall remarks
  const [pmsData, setPmsData] = useState({});
  const jwtToken = localStorage.getItem("token");

  const [remarksModalOpen, setRemarksModalOpen] = useState(false);
  const [activeKraIndex, setActiveKraIndex] = useState(null);
  const [activeKpiIndex, setActiveKpiIndex] = useState(null);
  const [managerRemark, setManagerRemark] = useState(""); // ✅ KPI-level remark input

  const { id: employeeId, manager: reportingManager } = useParams();

  useEffect(() => {
    if (employeeId && reportingManager) {
      loadKraKpi(employeeId, reportingManager);
    }
  }, [employeeId, reportingManager]);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    if (isNaN(date)) return dateString;
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  };

  const loadKraKpi = async (employeeId, reportingManager) => {
    try {
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/manager/kra-kpi/${reportingManager}/${employeeId}`,
        {
          headers: { Authorization: `Bearer ${jwtToken}` }
        }
      );
      const data = result.data;
      setKraKpi(data.kra || []);
      setEmployeeName(data.employee.name);
      setDepartment(data.employee.department);
      setDesignation(data.employee.currentDesignation);
      setDueDate(formatDate(data.dueDate || "2025-05-25"));
      setEmployeeReviewDate(formatDate(data.selfReviewDate || "2025-05-23"));
      setManagerReviewDate(formatDate(data.managerReviewDate || "2025-05-24"));
      setRemarks(data.remark || ""); // ✅ Load overall remarks
      setPmsData(data);
    } catch (error) {
      console.error("Failed to load KRA/KPI:", error);
    }
  };

  const handleInputChange = (kraIndex, kpiIndex, field, value) => {
    const updatedKraKpi = [...krakpi];
    const weightage = updatedKraKpi[kraIndex].kpi[kpiIndex].weightage;
    let numericValue = parseFloat(value) || 0;

    if (numericValue < 0) numericValue = 0;
    if (numericValue > weightage) numericValue = weightage;

    updatedKraKpi[kraIndex].kpi[kpiIndex][field] = numericValue;
    setKraKpi(updatedKraKpi);
  };

  const handleKpiRemarkSave = () => {
    if (activeKraIndex !== null && activeKpiIndex !== null) {
      const updatedKra = [...krakpi];
      updatedKra[activeKraIndex].kpi[activeKpiIndex].managerRemark = managerRemark; // ✅ Save KPI remark
      setKraKpi(updatedKra);
    }
    setRemarksModalOpen(false);
    setManagerRemark("");
    setActiveKraIndex(null);
    setActiveKpiIndex(null);
  };

  const openRemarksModal = (kraIndex, kpiIndex, currentRemark = "") => {
    setActiveKraIndex(kraIndex);
    setActiveKpiIndex(kpiIndex);
    setManagerRemark(currentRemark); // ✅ Pre-fill with existing KPI remark
    setRemarksModalOpen(true);
  };

  const calculateAverage = (kpi) => {
    const { selfScore = 0, managerScore = 0, review2 = 0 } = kpi;
    const hasReview2 = review2 && review2 !== 0;
    const scores = hasReview2 ? [selfScore, managerScore, review2] : [selfScore, managerScore];
    const total = scores.reduce((sum, score) => sum + score, 0);
    return (total / scores.length).toFixed(2);
  };

  const calculateOverallScores = () => {
    let totalWeightage = 0;
    let weightedSelf = 0;
    let weightedManager = 0;
    let weightedFinal = 0;

    krakpi.forEach((kra) => {
      kra.kpi.forEach((kpi) => {
        const weight = kpi.weightage;
        totalWeightage += weight;
        const selfScore = kpi.selfScore || 0;
        const managerScore = kpi.managerScore || 0;
        const review2 = kpi.review2 || 0;
        const hasReview2 = review2 && review2 !== 0;
        const average = hasReview2
          ? (selfScore + managerScore + review2) / 3
          : (selfScore + managerScore) / 2;

        weightedSelf += selfScore * weight;
        weightedManager += managerScore * weight;
        weightedFinal += average * weight;
      });
    });

    const round = (val) => (totalWeightage > 0 ? (val / totalWeightage).toFixed(2) : "0.00");

    return {
      selfScore: round(weightedSelf),
      managerScore: round(weightedManager),
      finalScore: round(weightedFinal),
    };
  };

  const { selfScore, managerScore, finalScore } = calculateOverallScores();

  const reviewSubmit = async () => {
    try {
      await submitReview(true);
      setErrorMessage("PMS Review is successfully submitted.");
      setTitle("PMS Review");
      setShowModal(true);
    } catch (err) {
      console.error("Error submitting review", err);
    }
  };

  const draftSave = async () => {
    try {
      await submitReview(false);
      setErrorMessage("PMS Review has been saved as a draft.");
      setTitle("PMS Review");
      setShowModal(true);
    } catch (err) {
      console.error("Error saving draft", err);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const printHandler = () => {
    window.print();
  };

  const submitReview = async (isSubmit) => {
    const payload = {
      employeeId,
      remark: remarks, // ✅ Overall remarks
      selfCompleted: pmsData.selfCompleted || true,
      managerCompleted: isSubmit,
      dueDate,
      managerReviewDate,
      selfReviewDate: employeeReviewDate,
      pmsInitiated: pmsData.pmsInitiated || false,
      review2: false,
      managerApproval: pmsData.managerApproval || false,
      kra: krakpi.map((kra) => ({
        kraId: kra.kraId,
        kraName: kra.kraName,
        weightage: kra.weightage,
        kpi: kra.kpi.map((kpi) => {
          const kpiPayload = {
            kpiId: kpi.id,
            description: kpi.description,
            weightage: kpi.weightage,
            selfScore: kpi.selfScore || 0,
            managerScore: kpi.managerScore || 0,
            managerRemark: kpi.managerRemark || "", // ✅ Include KPI remarks
          };
          if (kpi.review2 && kpi.review2 !== 0) {
            kpiPayload.review2 = kpi.review2;
          }
          return kpiPayload;
        }),
      })),
    };

    const result = await axios.patch(
      `${baseUrl}/api/v1/pms/manager/manager-review/${reportingManager}/${employeeId}`,
      payload,
      {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      }
    );

    console.log(result);
  };

  const isSubmitButtonDisabled = () => {
    if (pmsData.managerCompleted) return true;
    return !(
      pmsData.pmsInitiated === true &&
      pmsData.selfCompleted === true &&
      pmsData.managerApproval === true &&
      pmsData.managerCompleted === false
    );
  };

  return (
    <div className="prf-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}
      {remarksModalOpen && (
        <div className="remarks-modal-overlay">
          <div className="remarks-modal">
            <h3>Add KPI Remark</h3>
            <textarea
              value={managerRemark}
              onChange={(e) => setManagerRemark(e.target.value)} // ✅ KPI-level remark input
              placeholder="Enter your KPI remark here..."
              rows="5"
            />
            <div className="remarks-modal-actions">
              <button onClick={handleKpiRemarkSave}>Save</button>
              <button onClick={() => setRemarksModalOpen(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      <header className="prf-header">
        <div className="prf-title-section">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="per-icon home-icon" />
          </Link>
          <h1 className="prf-title">Performance Review Form</h1>
          <img src={logo} alt="Company Logo" className="prf-company-logo" />
        </div>

        <div className="prf-filters">
          <label>Employee Name: <input type="text" value={employeeName} readOnly /></label>
          <label>Designation: <input type="text" value={designation} readOnly /></label>
          <label>Department: <input type="text" value={department} readOnly /></label>
          <label>Due Date: <input type="text" value={dueDate} readOnly /></label>
          <label>Employee Review Date: <input type="text" value={employeeReviewDate} readOnly /></label>
          <label>Manager Review Date: <input type="text" value={managerReviewDate} readOnly /></label>
        </div>
      </header>

      <div className="prf-sections">
        {krakpi.length > 0 ? (
          krakpi.map((kra, kraIndex) => (
            <div key={kraIndex} className="prf-section">
              <div className="prf-section-header">
                <h3>KRA - {kra.kraName}</h3>
                <span>Weightage: {kra.weightage}</span>
              </div>
              <table className="prf-table">
                <thead>
                  <tr>
                    <th style={{ width: "5%" }}>KPI's</th>
                    <th style={{ width: "5%" }}>Weightage</th>
                    <th style={{ width: "5%" }}>Self Rating</th>
                    <th><span style={{ color: "red", fontSize: "20px", width: "3%" }}>*</span> Review-1</th>
                    <th style={{ width: "5%" }}>Average</th>
                    <th style={{ width: "5%" }}>Remarks</th>
                  </tr>
                </thead>
                <tbody>
                  {kra.kpi.map((kpi, kpiIndex) => (
                    <tr key={kpiIndex}>
                      <td>{kpi.description}</td>
                      <td>{kpi.weightage}</td>
                      <td>{kpi.selfScore}</td>
                      <td>
                        <input
                          type="number"
                          min="0"
                          max={kpi.weightage}
                          value={kpi.managerScore || ""}
                          onChange={(e) =>
                            handleInputChange(kraIndex, kpiIndex, "managerScore", e.target.value)
                          }
                          required
                        />
                      </td>
                      <td>
                        <input type="text" readOnly value={calculateAverage(kpi)} />
                      </td>
                      <td>
                        <BsChatText
                          className="remarks-btn"
                          onClick={() => openRemarksModal(kraIndex, kpiIndex, kpi.managerRemark || "")} // ✅ Show KPI remark
                        />
                        {kpi.managerRemark && <span className="remarks-preview">✔</span>} {/* ✅ Show check */}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ))
        ) : (
          <p>Loading KRA/KPI data...</p>
        )}
      </div>

      <div className="prf-overall-section">
        <div className="prf-overall-score">
          <h3>Overall Score</h3>
          <table className="prf-overall-table">
            <thead>
              <tr>
                <th>Category</th>
                <th>Self Score</th>
                <th>Review 1</th>
                <th>Final Score</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Total Score</td>
                <td>{selfScore}</td>
                <td>{managerScore}</td>
                <td>{finalScore}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="prf-overall-remarks">
          <h3>Overall Remarks</h3>
          <textarea
            placeholder="Enter your overall remarks..."
            value={remarks} // ✅ Overall remark
            onChange={(e) => setRemarks(e.target.value)}
          />
        </div>

        <div className="prf-actions">
          <button className="prf-print-btn" onClick={printHandler}>Print</button>
          <button className="prf-draft-btn" onClick={draftSave}>Save as Draft</button>
          <button
            className="prf-submit-review-btn"
            onClick={reviewSubmit}
            disabled={isSubmitButtonDisabled()}
          >
            Submit Review
          </button>
        </div>
      </div>
    </div>
  );
};

export default PerformanceReview;
