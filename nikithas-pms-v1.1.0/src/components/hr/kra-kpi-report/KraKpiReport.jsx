import React, { useState } from 'react';
import './KraKpiReport.css';

import logo from '../../../assets/images/nikithas-logo.png';
import { FaHome } from 'react-icons/fa';
import { Link } from 'react-router-dom';

const Performance = () => {
  const [selectedYear, setSelectedYear] = useState("2025"); 
  const [selectedQuarter, setSelectedQuarter] = useState(null); 
  const [selectedMonth, setSelectedMonth] = useState(null); 

  const [showQuarters, setShowQuarters] = useState(false); 
  const [hoveredQuarter, setHoveredQuarter] = useState(null); 
  const [hoverEnabled, setHoverEnabled] = useState(true); // ✅ new state

  // Dummy KPI Data
  const dummyData = {
    2025: [
      {
        kraId: 1,
        kraName: "Technical Skills",
        kpi: [
          {
            id: 101, 
            description: "Code Quality",
            selfScore: 8,
            managerScore: 7,
            employeeRemark: "Improved compared to last year",
            managerRemark: "Good work, but consistency required",
          },
          {
            id: 102,
            description: "Problem Solving",
            selfScore: 9,
            managerScore: 8,
            employeeRemark: "Enjoy solving new problems",
            managerRemark: "Excellent analytical ability",
          },
        ],
      },
      {
        kraId: 2,
        kraName: "Team Collaboration",
        kpi: [
          {
            id: 201,
            description: "Communication",
            selfScore: 7,
            managerScore: 8,
            employeeRemark: "Improving with practice",
            managerRemark: "Clear communication, keep it up",
          },
        ],
      },
    ],
    2024: [
      {
        kraId: 3,
        kraName: "Leadership",
        kpi: [
          {
            id: 301,
            description: "Guiding Juniors",
            selfScore: 8,
            managerScore: 7,
            employeeRemark: "Mentored 3 juniors",
            managerRemark: "Good guidance, scope to improve",
          },
        ],
      },
    ],
  };

  
  const quarters = ["Q1", "Q2", "Q3", "Q4"]; 
  const months = {
    Q1: ["April", "May", "June"],
    Q2: ["July", "August", "September"],
    Q3: ["October", "November", "December"],
    Q4: ["January", "February", "March"],
  };

  const handlePrint = () => {
    window.print();
  };

  const getAverageOutOf100 = (arr, key) => {
    if (!arr.length) return '0/100';
    const avg = arr.reduce((sum, kpi) => sum + kpi[key], 0) / arr.length;
    return `${Math.round(avg * 10)}/100`;
  };

  return (
    <div className="employee-module-container">
      {/* Header */}
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
        
        <div
          className="dropdown"
          onMouseEnter={() => hoverEnabled && setShowQuarters(true)} 
          onMouseLeave={() => {
            if (hoverEnabled) {
              setShowQuarters(false);
              setHoveredQuarter(null); 
            }
          }}
        >
          <select
            value={selectedYear}
            onFocus={() => setHoverEnabled(false)}   // ✅ disable hover while selecting year
            onBlur={() => setHoverEnabled(true)}    // ✅ re-enable after done
            onChange={(e) => {
              setSelectedYear(e.target.value);
              setSelectedQuarter(null);
              setSelectedMonth(null);
              setHoveredQuarter(null);
              setHoverEnabled(true); // ✅ ensure hover works after year is picked
            }}
          >
            {Object.keys(dummyData)
              .sort((a, b) => b - a)
              .map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
          </select>

          {/* ✅ Show quarters only when hover is enabled */}
          {hoverEnabled && showQuarters && (
            <div className="quarter-menu">
              {quarters.map((qtr) => (
                <div
                  key={qtr}
                  className="quarter-item"
                  onMouseEnter={() => setHoveredQuarter(qtr)}
                  onMouseLeave={() => setHoveredQuarter(null)}
                  onClick={() => {
                    setSelectedQuarter(qtr);
                    setSelectedMonth(null);
                  }}
                >
                  {qtr}

                  {hoveredQuarter === qtr && (
                    <div className="month-menu">
                      {months[qtr].map((m) => (
                        <div
                          key={m}
                          className="month-item"
                          onClick={(e) => {
                            e.stopPropagation(); 
                            setSelectedMonth(m);
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

        {dummyData[selectedYear]?.map((kraItem) => (
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
                    Self Rating:{" "}
                    {kpi.selfScore ? Math.round(kpi.selfScore * 10) : 0}/100
                  </div>
                  <div>
                    Manager Rating:{" "}
                    {kpi.managerScore ? Math.round(kpi.managerScore * 10) : 0}/100
                  </div>
                </div>

                <div className="employee-module-remarks">
                  <div>
                    Employee Remark: {kpi.employeeRemark || "No remark provided"}
                  </div>
                  <div>
                    Manager Remark: {kpi.managerRemark || "No remark provided"}
                  </div>
                </div>
              </div>
            ))}
          </div>
        ))}

        <div className="employee-module-overall-performance">
          <span>
            Average Self Rating:{" "}
            {(() => {
              const allKpis =
                dummyData[selectedYear]?.flatMap((kra) => kra.kpi) || [];
              return getAverageOutOf100(allKpis, "selfScore");
            })()}
          </span>
          <span>
            Average Manager Rating:{" "}
            {(() => {
              const allKpis =
                dummyData[selectedYear]?.flatMap((kra) => kra.kpi) || [];
              return getAverageOutOf100(allKpis, "managerScore");
            })()}
          </span>
        </div>
      </div>

      {/* Print Button */}
      <button className="employee-module-print-btn" onClick={handlePrint}>
        Print
      </button>
    </div>
  );
};

export default Performance;
