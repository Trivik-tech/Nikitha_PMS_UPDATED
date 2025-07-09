import React, { useEffect, useState } from "react";
import "./EmployeeReview.css";
import "./EmployeeReviewResponsiveness.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../modal/Modal";
import axios from "axios";
import { baseUrl } from '../../urls/CommenUrl'

const PerformanceReview = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [krakpi, setKraKpi] = useState([]);
  const [dueDate, setDueDate] = useState("");
  const [selfReviewDate, setSelfReviewDate] = useState("");
  const [employeeName, setEmployeeName] = useState("");
  const [designation, setDesignation] = useState("");
  const [department, setDepartment] = useState("");
  const [selfCompleted, setSelfCompleted] = useState(false); // <-- NEW STATE

  const { id: employeeId } = useParams();
  const token=localStorage.getItem('token')

  const handleSelfScoreChange = (kraIndex, kpiIndex, value) => {
    let numericValue = Number(value);
    const weightage = krakpi[kraIndex].kpi[kpiIndex].weightage;

    if (numericValue < 0) numericValue = 0;
    if (numericValue > weightage) numericValue = weightage;

    const updatedKra = [...krakpi];
    updatedKra[kraIndex].kpi[kpiIndex].selfScore = numericValue;
    setKraKpi(updatedKra);
  };

  const reviewSubmit = async () => {
    setErrorMessage("PMS Review is successfully completed.");
    setTitle(" ");
    setShowModal(true);
    await selfReview();
  };

  const draftSave = () => {
    setErrorMessage("PMS Review has been saved as a draft.");
    setTitle(" ");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const printHandler = () => {
    window.print();
  };

  useEffect(() => {
    const loadKraKpi = async () => {
      try {
        const result = await axios.get(`${baseUrl}/api/v1/pms/employee/kra-kpi-list/${employeeId}`,{
          headers:{
            Authorization: `Bearer ${token}`
          }
        });
        setKraKpi(result.data.kra);
        setDepartment(result.data.employee.department.name || "Engineering");
        setDesignation(result.data.employee.currentDesignation);
        setEmployeeName(result.data.employee.name);
        setDueDate(result.data.dueDate || "20/5/2025");
        setSelfReviewDate(result.data.selfReviewDate || "15/5/2025");
        setSelfCompleted(result.data.selfCompleted === true); // <-- SET SELF COMPLETED
      } catch (error) {
        console.error(error);
      }
    };

    loadKraKpi();
  }, [employeeId]);

  const selfReview = async () => {
    try {
      const payload = {
        employeeId: employeeId,
        remark: "",
        selfCompleted: true,
        managerCompleted: false,
        dueDate: dueDate,
        managerReviewDate: null,
        selfReviewDate: selfReviewDate,
        pmsInitiated: false,
        review2: false,
        managerApproval: false,
        kra: krakpi.map((kra) => ({
          kraId: kra.kraId,
          kraName: kra.kraName,
          weightage: kra.weightage,
          kpi: kra.kpi.map((kpi) => ({
            kpiId: kpi.id,
            description: kpi.description,
            weightage: kpi.weightage,
            selfScore: kpi.selfScore || 0,
            managerScore: kpi.managerScore || 0,
            review2: kpi.review2 || 0,
          })),
        })),
      };

      const result = await axios.put(`${baseUrl}/api/v1/pms/employee/self-review/${employeeId}`, payload,{
        headers:{
          Authorization: `Bearer ${token}`
        }
      });
      console.log("Review submitted:", payload);
      console.log(result.data);
    } catch (error) {
      console.error("Error submitting review:", error);
    }
  };

  return (
    <div className="employee-module-review-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}

      <header className="employee-module-review-header">
        <div className="employee-module-review-title-section">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="employee-module-review-icon home-icon" />
          </Link>
          <h1 className="employee-module-review-title">Performance Review Form</h1>
          <img src={logo} alt="Company Logo" className="employee-module-review-company-logo" />
        </div>

        <div className="employee-module-review-filters">
          <label>
            Due Date: <input type="text" value={dueDate} readOnly />
          </label>
          <label>
            Self Review Date: <input type="text" value={selfReviewDate} readOnly />
          </label>
          <label>
            Employee Name: <input type="text" value={employeeName} readOnly />
          </label>
          <label>
            Designation: <input type="text" value={designation} readOnly />
          </label>
          <label>
            Department: <input type="text" value={department} readOnly />
          </label>
        </div>
      </header>

      <div className="employee-module-review-sections">
        {krakpi.map((kra, kraIndex) => (
          <div key={kraIndex} className="employee-module-review-section">
            <div className="employee-module-review-section-inner">
              <div className="employee-module-review-section-header">
                <h3>KRA - {kra.kraName}</h3>
                <span>Weightage: {kra.weightage}</span>
              </div>
              <table className="employee-module-review-table">
                <thead>
                  <tr>
                    <th>KPI's</th>
                    <th style={{ width: "5%" }}>Weightage</th>
                    <th style={{ width: "5%" }}>Self Rating</th>
                  </tr>
                </thead>
                <tbody>
                  {kra.kpi.map((kpi, kpiIndex) => (
                    <tr key={kpiIndex}>
                      <td>{kpi.description}</td>
                      <td>{kpi.weightage}</td>
                      <td>
                        <input
                          type="number"
                          min="0"
                          max={kpi.weightage}
                          value={kpi.selfScore || ""}
                          onChange={(e) => handleSelfScoreChange(kraIndex, kpiIndex, e.target.value)}
                          disabled={selfCompleted} // <-- DISABLE ON SELF COMPLETED
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        ))}
      </div>

      <div className="employee-module-review-actions">
        <button className="employee-module-review-print-btn" onClick={printHandler}>Print</button>
        <button className="employee-module-review-draft-btn" onClick={draftSave} 
        disabled={selfCompleted}
        >Save as Draft</button>
        <button
          className="employee-module-review-submit-review-btn"
          onClick={reviewSubmit}
          disabled={selfCompleted} // <-- DISABLE ON SELF COMPLETED
        >
          Submit Review
        </button>
      </div>
    </div>
  );
};

export default PerformanceReview;