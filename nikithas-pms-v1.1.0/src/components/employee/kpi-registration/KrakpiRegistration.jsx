import React, { useState, useEffect } from "react";
import "./Krakpi.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import "./Animation.css";
import { Link, useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";
import Modal from "../../modal/Modal"; // Import Modal

const MAX_KRA = 5;
const MAX_KPI = 5;

const Krakpi = () => {
  const [kraList, setKraList] = useState([
    {
      kraName: "",
      kpi: [{ description: "" }],
    },
  ]);

  const [modalMessage, setModalMessage] = useState("");
  const [modalTitle, setModalTitle] = useState("");
  const [showModal, setShowModal] = useState(false);

  const [kraKpiAllowed, setKraKpiAllowed] = useState(true); // Track if employee can add KRA/KPI

  const { id: employeeId } = useParams();
  const navigate = useNavigate(); // <-- ADD THIS

  // State to track if we need to redirect after OK on modal
  const [redirectToDashboard, setRedirectToDashboard] = useState(false);

  // If limit is reached, pop up automatically even before clicking "Save"
  useEffect(() => {
    if (kraList.length > MAX_KRA) {
      setKraList((prev) => prev.slice(0, MAX_KRA));
      openModal("Limit Reached", `You can only add up to ${MAX_KRA} KRAs.`);
    }
    // Check KPIs per KRA
    kraList.forEach((kra, idx) => {
      if (kra.kpi.length > MAX_KPI) {
        setKraList((prev) => {
          const updated = [...prev];
          updated[idx].kpi = updated[idx].kpi.slice(0, MAX_KPI);
          return updated;
        });
        openModal("Limit Reached", `You can only add up to ${MAX_KPI} KPIs per KRA.`);
      }
    });
    // eslint-disable-next-line
  }, [kraList]);

  const openModal = (title, message, redirect = false) => {
    setModalTitle(title);
    setModalMessage(message);
    setShowModal(true);
    setRedirectToDashboard(redirect); // set flag for redirect
  };

  const closeModal = () => {
    setShowModal(false);
    setModalMessage("");
    setModalTitle("");
    // If modal was for Success, Error, Draft Saved, Already Registered, go to dashboard
    if (redirectToDashboard) {
      setRedirectToDashboard(false); // reset
      navigate("/employee-dashboard");
    }
  };

  const handleKRAChange = (index, value) => {
    const updatedList = [...kraList];
    updatedList[index].kraName = value;
    setKraList(updatedList);
  };

  const handleKPIChange = (kraIndex, kpiIndex, field, value) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpi[kpiIndex][field] = value;
    setKraList(updatedList);
  };

  const addKRA = () => {
    if (kraList.length >= MAX_KRA) {
      openModal("Limit Reached", `You can only add up to ${MAX_KRA} KRAs.`);
      return;
    }
    setKraList([
      ...kraList,
      {
        kraName: "",
        kpi: [{ description: "" }],
      },
    ]);
  };

  const removeKRA = (index) => {
    const updatedList = [...kraList];
    updatedList.splice(index, 1);
    setKraList(updatedList);
  };

  const addKPI = (kraIndex) => {
    if (kraList[kraIndex].kpi.length >= MAX_KPI) {
      openModal("Limit Reached", `You can only add up to ${MAX_KPI} KPIs per KRA.`);
      return;
    }
    const updatedList = [...kraList];
    updatedList[kraIndex].kpi.push({ description: "" });
    setKraList(updatedList);
  };

  const removeKPI = (kraIndex, kpiIndex) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpi.splice(kpiIndex, 1);
    setKraList(updatedList);
  };

  const handleSubmit = async () => {
    // Prevent submission if limit is exceeded
    if (kraList.length > MAX_KRA) {
      openModal("Limit Reached", `You can only add up to ${MAX_KRA} KRAs.`);
      return;
    }
    for (let i = 0; i < kraList.length; i++) {
      if (kraList[i].kpi.length > MAX_KPI) {
        openModal("Limit Reached", `You can only add up to ${MAX_KPI} KPIs per KRA.`);
        return;
      }
    }

    try {
      const payload = {
        employeeId: employeeId || "EMP1212",
        remark: "",
        selfCompleted: false,
        managerCompleted: false,
        dueDate: "",
        managerReviewDate: null,
        selfReviewDate: null,
        pmsInitiated: false,
        review2: false,
        managerApproval: false,
        kra: kraList.map((kra) => ({
          kraName: kra.kraName,
          weightage: 0,
          kpi: kra.kpi.map((kpiItem) => ({
            description: kpiItem.description,
            weightage: 0,
            selfScore: 0,
            managerScore: 0,
            review2: 0,
          })),
        })),
      };

      const response = await axios.post(
        `${baseUrl}/api/v1/pms/employee/register-kra-kpi`,
        payload
      );

      openModal("Success", "KRA/KPI submitted successfully!", true); // <--- redirect after OK
    } catch (error) {
      console.error("Submission error:", error);
      openModal("Error", "Submission failed.", true); // <--- redirect after OK
    }
  };

  const handleDraft = () => {
    openModal("Draft Saved", "KRA/KPI saved as draft!", true); // <--- redirect after OK
  };

  useEffect(() => {
    const checkKraKpiAlreadyExists = async () => {
      if (!employeeId) return;
      try {
        const result = await axios.get(
          `${baseUrl}/api/v1/pms/employee/check-kra-kpi/${employeeId}`
        );
        // Assuming result.data.status indicates if KRA/KPI is already registered
        if (result.data && result.data.status === true) {
          // Not allowed: show popup and block form, redirect after OK
          setKraKpiAllowed(false);
          openModal(
            "Already Registered",
            "KRA/KPI is already registered for this employee.",
            true // <--- redirect after OK
          );
        } else {
          setKraKpiAllowed(true);
        }
      } catch (error) {
        console.log(error.message);
        
      }
    };
    checkKraKpiAlreadyExists();
    // eslint-disable-next-line
  }, [employeeId]);

  // If not allowed, render only popup and return null for the form
  if (!kraKpiAllowed) {
    return (
      <>
        {showModal && (
          <Modal
            title={modalTitle}
            message={modalMessage}
            closeModal={closeModal}
          />
        )}
      </>
    );
  }

  return (
    <>
      <div className="employee-module-fullscreen-wrapper employee-module-fade-in">
        <div className="employee-module-container">
          <div className="employee-module-header-title">
            <Link to="/employee-dashboard" className="icon-link">
              <FaHome className="employee-module-home-icon" />
            </Link>
            <h2 className="employee-module-page-title">
              Employee's KRA/KPI Registration Form
            </h2>
            <img
              src={logo}
              alt="Company Logo"
              className="employee-module-logo"
            />
          </div>

          <button
            className="employee-module-add-kra employee-module-btn-hover"
            onClick={addKRA}
            disabled={kraList.length >= MAX_KRA}
          >
            Add New KRA
          </button>

          {kraList.map((kraItem, kraIndex) => (
            <div
              className="employee-module-kra-box employee-module-slide-in"
              key={kraIndex}
            >
              <div className="employee-module-kra-header">
                <input
                  type="text"
                  placeholder="Enter KRA"
                  value={kraItem.kraName}
                  onChange={(e) => handleKRAChange(kraIndex, e.target.value)}
                  className="employee-module-kra-input"
                />
                <button
                  className="employee-module-remove-btn employee-module-btn-hover"
                  onClick={() => removeKRA(kraIndex)}
                >
                  Remove KRA
                </button>
              </div>

              {kraItem.kpi.map((kpiItem, kpiIndex) => (
                <div
                  className="employee-module-kpi-box employee-module-slide-in"
                  key={kpiIndex}
                >
                  <input
                    type="text"
                    placeholder="Enter KPI"
                    value={kpiItem.description}
                    onChange={(e) =>
                      handleKPIChange(
                        kraIndex,
                        kpiIndex,
                        "description",
                        e.target.value
                      )
                    }
                    className="employee-module-kpi-input"
                  />
                  <button
                    className="employee-module-remove-btn employee-module-btn-hover"
                    onClick={() => removeKPI(kraIndex, kpiIndex)}
                  >
                    Remove KPI
                  </button>
                </div>
              ))}

              <button
                className="employee-module-add-kpi employee-module-btn-hover"
                onClick={() => addKPI(kraIndex)}
                disabled={kraItem.kpi.length >= MAX_KPI}
              >
                Add KPI
              </button>
            </div>
          ))}

          <div className="employee-module-action-buttons">
            <button
              className="employee-module-draft-btn employee-module-btn-hover"
              onClick={handleDraft}
            >
              Save as Draft
            </button>
            <button
              className="employee-module-submit-btn employee-module-btn-hover"
              onClick={handleSubmit}
            >
              Submit
            </button>
          </div>
        </div>
      </div>

      {showModal && (
        <Modal
          title={modalTitle}
          message={modalMessage}
          closeModal={closeModal}
        />
      )}
    </>
  );
};

export default Krakpi;