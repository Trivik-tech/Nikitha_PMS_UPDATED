import React, { useEffect, useState } from 'react';
import './Performance.css';
import './ResponseEmployeePerformance.css';
import logo from '../../../assets/images/nikithas-logo.png';
import { FaHome } from 'react-icons/fa';
import { Link, useParams } from 'react-router-dom';
import { baseUrl } from '../../urls/CommenUrl';
import axios from 'axios';

const Performance = () => {
  const [kraKpis, setKraKpis] = useState([]);
  const token = localStorage.getItem('token');
  const { id: employeeId } = useParams();

  const handlePrint = () => {
    window.print();
  };

  useEffect(() => {
    loadKraKpis();
  }, []);

  const loadKraKpis = async () => {
    try {
      const result = await axios.get(
        `${baseUrl}/api/v1/pms/employee/krakpi-list/${employeeId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(result.data.krakpis);
      setKraKpis(result.data.krakpis || []);
    } catch (error) {
      console.log(error);
    }
  };

  // ✅ Group KPIs by createdAt year instead of dateOfJoining
  const groupedByYear = kraKpis.reduce((acc, item) => {
    const year = new Date(item.createdAt).getFullYear();
    if (!acc[year]) acc[year] = [];
    acc[year].push(item);
    return acc;
  }, {});

  // Helper function to get average out of 100
  const getAverageOutOf100 = (arr, key) => {
    if (!arr.length) return '0/100';
    const avg = arr.reduce((sum, kpi) => sum + (kpi[key] || 0), 0) / arr.length;
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
        <h1 className="employee-module-per-page-title">Performance Summary</h1>
        <img src={logo} alt="Logo" className="employee-module-company-logo" />
      </div>

      {/* Performance by Year */}
      {Object.keys(groupedByYear)
        .sort((a, b) => b - a) // latest year first
        .map((year) => (
          <div key={year} className="employee-module-section">
            <h2>Year: {year}</h2>

            {groupedByYear[year].map((item) =>
              item.kra.map((kraItem) => (
                <div className="employee-module-kra" key={kraItem.kraId}>
                  {/* KRA Title */}
                  <div
                    className="employee-module-kra-title"
                    style={{ marginTop: '10px', marginBottom: '8px' }}
                  >
                    KRA: {kraItem.kraName}
                  </div>

                  {/* KPI List */}
                  {kraItem.kpi.map((kpi) => (
                    <div key={kpi.id} className="employee-module-kpi-block">
                      <div className="employee-module-kra-details">
                        KPI: {kpi.description}
                      </div>

                      {/* Ratings */}
                      <div className="employee-module-rating">
                        <div>
                          Self Rating:{' '}
                          {kpi.selfScore
                            ? Math.round(kpi.selfScore * 10)
                            : 0}
                          /100
                        </div>
                        <div>
                          Manager Rating:{' '}
                          {kpi.managerScore
                            ? Math.round(kpi.managerScore * 10)
                            : 0}
                          /100
                        </div>
                      </div>

                      {/* Remarks */}
                      <div className="employee-module-remarks">
                        <div>
                          Employee Remark:{' '}
                          {kpi.employeeRemark || 'No remark provided'}
                        </div>
                        <div>
                          Manager Remark:{' '}
                          {kpi.managerRemark || 'No remark provided'}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              ))
            )}

            {/* Yearly Average */}
            <div className="employee-module-overall-performance">
              <span>
                Average Self Rating:{' '}
                {(() => {
                  const allKpis = groupedByYear[year]
                    .flatMap((item) => item.kra)
                    .flatMap((kra) => kra.kpi);
                  return getAverageOutOf100(allKpis, 'selfScore');
                })()}
              </span>
              <span>
                Average Manager Rating:{' '}
                {(() => {
                  const allKpis = groupedByYear[year]
                    .flatMap((item) => item.kra)
                    .flatMap((kra) => kra.kpi);
                  return getAverageOutOf100(allKpis, 'managerScore');
                })()}
              </span>
            </div>
          </div>
        ))}

      {/* Print Button */}
      <button className="employee-module-print-btn" onClick={handlePrint}>
        Print
      </button>
    </div>
  );
};

export default Performance;
