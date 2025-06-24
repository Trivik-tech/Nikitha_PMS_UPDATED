import React, { useState } from "react";
import "./Krakpi.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import "./Animation.css";
import { Link } from "react-router-dom";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";

const Krakpi = () => {
  const [kraList, setKraList] = useState([
    {
      kraName: "",
      kpi: [
        {
          description: "",
        },
      ],
    },
  ]);

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
    setKraList([
      ...kraList,
      {
        kraName: "",
        kpi: [
          {
            description: "",
          },
        ],
      },
    ]);
  };

  const removeKRA = (index) => {
    const updatedList = [...kraList];
    updatedList.splice(index, 1);
    setKraList(updatedList);
  };

  const addKPI = (kraIndex) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpi.push({
      description: "",
    });
    setKraList(updatedList);
  };

  const removeKPI = (kraIndex, kpiIndex) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpi.splice(kpiIndex, 1);
    setKraList(updatedList);
  };

  const handleSubmit = async () => {
    try {
      const payload = {
        employeeId: "EMP1236",
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

      console.log(payload);
      const response = await axios.post(
        `${baseUrl}/api/v1/pms/employee/register-kra-kpi`,
        payload
      );

      console.log("Submitted successfully:", response.data);
      alert("KRA/KPI submitted successfully!");
    } catch (error) {
      console.error("Submission error:", error);
      alert("Submission failed. Please check the console for errors.");
    }
  };

  const handleDraft = () => {
    console.log("Saving as Draft:", kraList);
    alert("KRA/KPI saved as draft!");
  };

  return (
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
  );
};

export default Krakpi;
