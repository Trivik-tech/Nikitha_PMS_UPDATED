import React, { useState } from "react";
import "./Krakpi.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import "./Animation.css"; // Animation classes live here ✅
import { Link } from "react-router-dom";

const Krakpi = () => {
  const [kraList, setKraList] = useState([
    {
      kra: "",
      kraWeightage: "", // 👈 Added field
      kpis: [
        {
          kpi: "",
          weightage: "",
        },
      ],
    },
  ]);

  const handleKRAChange = (index, value) => {
    const updatedList = [...kraList];
    updatedList[index].kra = value;
    setKraList(updatedList);
  };

  const handleKRAWeightageChange = (index, value) => {
    const updatedList = [...kraList];
    updatedList[index].kraWeightage = value;
    setKraList(updatedList);
  };

  const handleKPIChange = (kraIndex, kpiIndex, field, value) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpis[kpiIndex][field] = value;
    setKraList(updatedList);
  };

  const addKRA = () => {
    setKraList([
      ...kraList,
      {
        kra: "",
        kraWeightage: "",
        kpis: [
          {
            kpi: "",
            weightage: "",
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
    updatedList[kraIndex].kpis.push({
      kpi: "",
      weightage: "",
    });
    setKraList(updatedList);
  };

  const removeKPI = (kraIndex, kpiIndex) => {
    const updatedList = [...kraList];
    updatedList[kraIndex].kpis.splice(kpiIndex, 1);
    setKraList(updatedList);
  };

  const handleSubmit = () => {
    console.log("Submitting Data:", kraList);
    alert("KRA/KPI Submitted Successfully!");
  };

  const handleDraft = () => {
    console.log("Saving as Draft:", kraList);
    alert("KRA/KPI Saved as Draft!");
  };

  return (
    <div className="employee-module-fullscreen-wrapper employee-module-fade-in">
      <div className="employee-module-container">
        <div className="employee-module-header-title">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="employee-module-home-icon" />
          </Link>
          <h2 className="employee-module-page-title">Employee's KRA/KPI Registration Form</h2>
          <img src={logo} alt="Company Logo" className="employee-module-logo" />
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
                value={kraItem.kra}
                onChange={(e) => handleKRAChange(kraIndex, e.target.value)}
                className="employee-module-kra-input"
              />
              <input
                type="text"
                placeholder="Enter KRA Weightage"
                value={kraItem.kraWeightage}
                onChange={(e) =>
                  handleKRAWeightageChange(kraIndex, e.target.value)
                }
                className="employee-module-weightage-input"
              />
              <button
                className="employee-module-remove-btn employee-module-btn-hover"
                onClick={() => removeKRA(kraIndex)}
              >
                Remove KRA
              </button>
            </div>

            {kraItem.kpis.map((kpiItem, kpiIndex) => (
              <div
                className="employee-module-kpi-box employee-module-slide-in"
                key={kpiIndex}
              >
                <input
                  type="text"
                  placeholder="Enter KPI"
                  value={kpiItem.kpi}
                  onChange={(e) =>
                    handleKPIChange(kraIndex, kpiIndex, "kpi", e.target.value)
                  }
                  className="employee-module-kpi-input"
                />
                <input
                  type="text"
                  placeholder="Enter Weightage"
                  value={kpiItem.weightage}
                  onChange={(e) =>
                    handleKPIChange(
                      kraIndex,
                      kpiIndex,
                      "weightage",
                      e.target.value
                    )
                  }
                  className="employee-module-weightage-input"
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
