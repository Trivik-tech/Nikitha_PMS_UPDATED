import React, { useEffect, useState, useCallback } from "react";
import "./KraKpiReport.css";
import logo from "../../../assets/images/nikithas-logo.png";
import { FaHome } from "react-icons/fa";
import { Link } from "react-router-dom";
import axios from "axios";
import { baseUrl } from "../../urls/CommenUrl";

const Performance = () => {
  const [selectedYear, setSelectedYear] = useState(""); // ⬅ start empty
  const [kraKpi, setKraKpi] = useState([]);
  const [selectedQuarter, setSelectedQuarter] = useState(null);
  const [selectedMonth, setSelectedMonth] = useState(null);
  const [showQuarters, setShowQuarters] = useState(false);
  /* eslint-disable no-unused-vars */
  const [hoveredQuarter, setHoveredQuarter] = useState(null);
  /* eslint-enable no-unused-vars */
  const [showMonthsFor, setShowMonthsFor] = useState(null); // ⬅ track quarter hover

  const token = localStorage.getItem("token");

  const quarters = ["Q1", "Q2", "Q3", "Q4"];
  const months = {
    Q1: ["April", "May", "June"],
    Q2: ["July", "August", "September"],
    Q3: ["October", "November", "December"],
    Q4: ["January", "February", "March"],
  };

  const handlePrint = () => window.print();

  const getAverageOutOf100 = (arr, key) => {
    if (!arr.length) return "0/100";
    const avg = arr.reduce((sum, kpi) => sum + (kpi[key] || 0), 0) / arr.length;
    return `${Math.round(avg * 10)}/100`;
  };

  const loadKraKpiYearly = useCallback(async (year) => {
    if (!year) return; // nothing until a year is selected
    try {
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/krakpi/kra-kpi-year-wise?empId=EMP1235&year=${year}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setKraKpi(result.data.krakpi || []);
    } catch (error) {
      console.error(error);
    }
  }, [token]);

  const loadKraKpiQuarterly = async (year, quarter) => {
    if (!quarter || !year) return; // nothing until a year is selected
    try {
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/krakpi/kra-kpi-quarter-wise?empId=EMP1235&year=${year}&quarter=${quarter}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setKraKpi(result.data.krakpi || []);
      // console.log(result.data);
    } catch (error) {
      console.error(error);
    }
  };

  const loadKraKpiMonthly = async (year, quarter, month) => {
    if (!quarter || !year || !month) return; // nothing until a year is selected
    try {
      let m = 0;
      switch (month) {
        case "January":
          m = 1;
          break;
        case "February":
          m = 2;
          break;
        case "March":
          m = 3;
          break;
        case "April":
          m = 4;
          break;
        case "May":
          m = 5;
          break;
        case "June":
          m = 6;
          break;
        case "July":
          m = 7;
          break;
        case "August":
          m = 8;
          break;
        case "September":
          m = 9;
          break;
        case "October":
          m = 10;
          break;
        case "November":
          m = 11;
          break;
        case "December":
          m = 12;
          break;
        default:
          m = 0; // or handle invalid input
      }

      const result = await axios.get(
        `${baseUrl}/api/v1/pms/krakpi/kra-kpi-month-wise?empId=EMP1235&year=${year}&quarter=${quarter}&month=${m}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setKraKpi(result.data.krakpi || []);
      // console.log(result.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    loadKraKpiYearly(2025);
  }, [loadKraKpiYearly]);

  return (
    <div className="employee-module-container">
      <div className="employee-module-header">
        <div className="employee-module-home-icon">
          <Link to="/employee-dashboard" className="icon-link">
            <FaHome className="per-icon home-icon" />
          </Link>
        </div>
        <h1 className="employee-module-per-page-title">Anil Kumar</h1>
        <img src={logo} alt="Logo" className="employee-module-company-logo" />
      </div>

      {/* Year / Quarter / Month Menu */}
      <div className="employee-module-filters">
        <label>Select Year: </label>

        {/* === Year selector === */}
        <div
          className="dropdown"
          style={{ display: "inline-block", position: "relative" }}
          // Hover menu only works if a year is selected
          onMouseEnter={() => {
            if (selectedYear)
              setShowQuarters(true);
          }}
          onMouseLeave={() => {
            setShowQuarters(false);
            setHoveredQuarter(null);
            setShowMonthsFor(null);
          }}
        >
          <select
            value={selectedYear}
            onChange={(e) => {
              const year = e.target.value;
              setSelectedYear(year);
              setSelectedQuarter(null);
              setSelectedMonth(null);
              setHoveredQuarter(null);
              setShowQuarters(false); // reset on year change
            }}
          >
            {/* <option value="">--Select--</option> */}
            <option value="2025">2025</option>
            <option value="2024">2024</option>
            <option value="2023">2023</option>
          </select>

          {/* === Quarter hover menu (enabled only after year chosen) === */}
          {showQuarters && (
            <div className="quarter-menu">
              {quarters.map((qtr) => (
                <div
                  key={qtr}
                  className="quarter-item"
                  onMouseEnter={() => {
                    // allow month hover only if quarter already clicked/selected
                    setHoveredQuarter(qtr);
                    if (selectedQuarter === qtr) setShowMonthsFor(qtr);
                  }}
                  onClick={(e) => {
                    e.stopPropagation();
                    setSelectedQuarter(qtr);
                    setSelectedMonth(null);
                    setShowMonthsFor(qtr); // enable months after selecting quarter
                    loadKraKpiQuarterly(selectedYear, qtr);
                  }}
                >
                  {qtr}
                  {/* === Month hover menu (enabled only after quarter clicked) === */}
                  {showMonthsFor === qtr && (
                    <div className="month-menu">
                      {months[qtr].map((m) => (
                        <div
                          key={m}
                          className="month-item"
                          onClick={(e) => {
                            e.stopPropagation();
                            setSelectedMonth(m);
                            loadKraKpiMonthly(selectedYear, selectedQuarter, m);
                          }}
                        >
                          {m}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Performance Data */}
      <div className="employee-module-section">
        <h2>
          {selectedYear}
          {selectedQuarter ? ` - ${selectedQuarter}` : ""}
          {selectedMonth ? ` - ${selectedMonth}` : ""}
        </h2>

        {kraKpi.map((record) => (
          <div key={record.id}>
            {record.kra.map((kraItem) => (
              <div className="employee-module-kra" key={kraItem.kraId}>
                <div className="employee-module-kra-title">
                  KRA: {kraItem.kraName}
                </div>
                {kraItem.kpi.map((kpi) => (
                  <div key={kpi.id} className="employee-module-kpi-block">
                    <div className="employee-module-kra-details">
                      KPI: {kpi.description}
                    </div>
                    <div className="employee-module-rating">
                      <div>
                        Self Rating: {Math.round(kpi.selfScore * 10)}/100
                      </div>
                      <div>
                        Manager Rating: {Math.round(kpi.managerScore * 10)}/100
                      </div>
                    </div>
                    <div className="employee-module-remarks">
                      <div>
                        Employee Remark:{" "}
                        {kpi.employeeRemark || "No remark provided"}
                      </div>
                      <div>
                        Manager Remark:{" "}
                        {kpi.managerRemark || "No remark provided"}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ))}
          </div>
        ))}

        <div className="employee-module-overall-performance">
          <span>
            Average Self Rating:{" "}
            {getAverageOutOf100(
              kraKpi.flatMap((rec) => rec.kra.flatMap((k) => k.kpi)),
              "selfScore"
            )}
          </span>
          <span>
            Average Manager Rating:{" "}
            {getAverageOutOf100(
              kraKpi.flatMap((rec) => rec.kra.flatMap((k) => k.kpi)),
              "managerScore"
            )}
          </span>
        </div>
      </div>

      <button className="employee-module-print-btn" onClick={handlePrint}>
        Print
      </button>
    </div>
  );
};

export default Performance;
