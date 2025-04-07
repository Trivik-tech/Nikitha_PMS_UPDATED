import { useState } from "react";
import "./Krakpi.css"; // Import the CSS file
import logo from "../../../assets/images/nikithas-logo.png"; // Import logo

export default function KRAKPIForm() {
  const [kras, setKras] = useState([
    { id: 1, title: "", kpis: [{ id: 1, text: "", marks: "" }], weightage: "" },
  ]);

  const addKRA = () => {
    setKras([
      ...kras,
      { id: kras.length + 1, title: "", kpis: [{ id: 1, text: "", marks: "" }], weightage: "" },
    ]);
  };

  const removeKRA = (id) => {
    setKras(kras.filter((kra) => kra.id !== id));
  };

  const addKPI = (kraId) => {
    setKras(
      kras.map((kra) =>
        kra.id === kraId
          ? { ...kra, kpis: [...kra.kpis, { id: kra.kpis.length + 1, text: "", marks: "" }] }
          : kra
      )
    );
  };

  const removeKPI = (kraId, kpiId) => {
    setKras(
      kras.map((kra) =>
        kra.id === kraId ? { ...kra, kpis: kra.kpis.filter((kpi) => kpi.id !== kpiId) } : kra
      )
    );
  };

  return (
    <div className="container">
      <div className="form-box">
        {/* Logo added above the heading */}
        <img src={logo} alt="Company Logo" className="krakpi-logo" />
        
        <h2>Employee's KRA/KPI Registration Form</h2>

        {/* <div className="employee-info">
          <input type="text" placeholder="Enter employee name" />
          <input type="text" placeholder="Enter employee code" />
          <input type="text" placeholder="Enter designation" />
        </div> */}

        <button className="add-kra" onClick={addKRA}>
          Add New KRA
        </button>
        {kras.map((kra) => (
          <div key={kra.id} className="kra-box">
            <div className="kra-header">
              <input type="text" className="kra-input" placeholder="Enter KRA " />
              <input type="number" className="weightage-input" placeholder="KRA Weightage" />
              <button className="remove-btn" onClick={() => removeKRA(kra.id)}>
                Remove
              </button>
            </div>
            {kra.kpis.map((kpi) => (
              <div key={kpi.id} className="kpi-box">
                <input type="text" className="kpi-input" placeholder="Enter KPI" />
                <input type="number" className="weightage-input" placeholder="KPI Weightage" />
                <button className="remove-btn" onClick={() => removeKPI(kra.id, kpi.id)}>
                  Remove
                </button>
              </div>
            ))}
            <button className="add-kpi" onClick={() => addKPI(kra.id)}>
              + Add KPI
            </button>
          </div>
        ))}
        <div className="action-buttons">
          <button className="draft-btn">Save as Draft</button>
          <button className="submit-btn">Submit</button>
        </div>
      </div>
    </div>
  );
}
