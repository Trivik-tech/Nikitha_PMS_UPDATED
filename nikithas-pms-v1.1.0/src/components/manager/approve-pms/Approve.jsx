// Approve.jsx
import React, { useEffect, useState } from "react";
import "./Approve.css";
import "./ResponsiveApprovePms.css"
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../../components/modal/Modal";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";

const Approve = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [krakpi, setKraKpis] = useState([]);
  const [name, setName] = useState("");
  const [designation, setDesignation] = useState("");
  const [department, setDepartment] = useState("");
  const [manager, setManager] = useState("");
  const [loading, setLoading] = useState(false);

  const emplId = useParams();

  useEffect(() => {
    const loadKraKpis = async () => {
      try {
        const result = await axios.get(
          `${baseUrl}/api/v1/pms/manager/kra-kpi/Pradeep Prahalada Rao Kubair/${emplId.id}`
        );
        setKraKpis(result.data.kra || []);
        setName(result.data.employee.name);
        setDesignation(result.data.employee.currentDesignation);
        setDepartment(result.data.employee.department);
        setManager(result.data.employee.reportingManager);
      } catch (error) {
        console.error("Error loading KRA/KPI data:", error);
      }
    };

    loadKraKpis();
  }, [emplId]);

  const handleKRAWeightageChange = (index, value) => {
    const updated = [...krakpi];
    updated[index].weightage = value;
    setKraKpis(updated);
  };

  const handleKPIWeightageChange = (kraIndex, kpiIndex, value) => {
    const updated = [...krakpi];
    updated[kraIndex].kpi[kpiIndex].weightage = value;
    setKraKpis(updated);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  // New function to save updated KRA/KPI
  const handleSaveChanges = async () => {
    setLoading(true);
    try {
      const payload = {
        employeeId: emplId.id,
        remark: "",
        selfCompleted: false,
        managerCompleted: false,
        dueDate: null,
        managerReviewDate: null,
        selfReviewDate:null,
        pmsInitiated: false,
        review2: false,
        managerApproval: true,
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

      console.log(payload)
      const result= await axios.patch(
        `${baseUrl}/api/v1/pms/manager/approve-krakpi/${emplId.id}/Pradeep Prahalada Rao Kubair`,
        payload
      );
      console.log(result.data)

      setErrorMessage("KRA/KPI updates saved successfully.");
      setTitle("Save Successful");
      setShowModal(true);
    } catch (error) {
      console.error("Error saving KRA/KPI updates:", error);
      setErrorMessage("Failed to save updates.");
      setTitle("Save Failed");
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const rejectKraKpi = async () => {
    try {
      await axios.patch(
        `${baseUrl}/api/v1/pms/manager/approve-krakpi/${emplId.id}/Pradeep Prahalada Rao Kubair`,
        { approveStatus: false }
      );
      setErrorMessage("PMS has been rejected successfully.");
      setTitle("PMS Reject");
      setShowModal(true);
    } catch (error) {
      console.error("Rejection error:", error);
      setErrorMessage("Error occurred during rejection.");
      setTitle("Rejection Error");
      setShowModal(true);
    }
  };

  const handleApproveClick = async () => {
    // Optionally, save changes first before approval
    await handleSaveChanges();
    
  };

  const handleRejectClick = async () => {
    // Optionally, save changes first before rejection
    await handleSaveChanges();
    await rejectKraKpi();
  };

  return (
    <div className="manager-approve-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}

      <header className="manager-approve-header">
        <div className="manager-approve-title-section">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="icon home-icon" />
          </Link>
          <h1 className="manager-approve-title">Performance Approval</h1>
          <img src={logo} alt="Company Logo" className="manager-approve-logo" />
        </div>

        <div className="manager-approve-filters">
          <label>
            Employee ID: <input type="text" value={emplId.id} readOnly />
          </label>
          <label>
            Employee Name: <input type="text" value={name} readOnly />
          </label>
          <label>
            Designation: <input type="text" value={designation} readOnly />
          </label>
          <label>
            Department: <input type="text" value={department} readOnly />
          </label>
          <label>
            Manager: <input type="text" value={manager} readOnly />
          </label>
        </div>
      </header>

      <div className="manager-approve-sections">
        {krakpi.map((kra, kraIndex) => (
          <div key={kraIndex} className="manager-approve-section">
            <div className="manager-approve-section-header">
              <h3>KRA - {kra.kraName}</h3>
              <div className="kra-weightage-container">
                <input
                  type="text"
                  value={kra.weightage||""}
                  onChange={(e) =>
                    handleKRAWeightageChange(kraIndex, e.target.value)
                  }
                  placeholder="KRA Weightage"
                  className="manager-weightage-input"
                />
              </div>
            </div>

            <table className="manager-approve-table">
              <thead>
                <tr>
                  <th>KPI Description</th>
                  <th>Weightage</th>
                </tr>
              </thead>
              <tbody>
                {kra.kpi.map((kpiItem, kpiIndex) => (
                  <tr key={kpiIndex}>
                    <td>{kpiItem.description}</td>
                    <td>
                      <input
                        type="text"
                        value={kpiItem.weightage||""}
                        onChange={(e) =>
                          handleKPIWeightageChange(
                            kraIndex,
                            kpiIndex,
                            e.target.value
                          )
                        }
                        placeholder="KPI Weightage"
                        className="manager-weightage-input"
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>

      <div className="manager-approve-actions">
        <button
          className="manager-approve-submit-btn approve-btn"
          onClick={handleApproveClick}
          disabled={loading}
        >
          Approve
        </button>
        <button
          className="manager-approve-submit-btn reject-btn"
          onClick={handleRejectClick}
          disabled={loading}
        >
          Reject
        </button>
      </div>
    </div>
  );
};

export default Approve;
