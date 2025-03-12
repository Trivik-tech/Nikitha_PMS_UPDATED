import { useState } from "react";
import "./EmployeeList.css";


const EmployeeDetails = () => {
  const [activeTab, setActiveTab] = useState("employment");

  return (
    <div className="container">
      <h2 className="header">Employee Details</h2>

      {/* Tabs */}
      <div className="tabs">
        <button
          className={`tab ${activeTab === "employment" ? "active" : ""}`}
          onClick={() => setActiveTab("employment")}
        >
          Employment Information
        </button>
        <button
          className={`tab ${activeTab === "personal" ? "active" : ""}`}
          onClick={() => setActiveTab("personal")}
        >
          Personal Information
        </button>
      </div>

      {/* Form */}
      {activeTab === "employment" ? (
        <div className="form">
          <div className="form-group">
            <label>Employee ID</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Designation</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>First Name</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Last Name</label>
            <input type="text" />
          </div>

          <div className="form-group">
            <label>Role</label>
            <select>
              <option>Developer</option>
              <option>Team Lead</option>
              <option>Project Manager</option>
              <option>QA Engineer</option>
            </select>
          </div>
          <div className="form-group">
            <label>Department</label>
            <select>
              <option>Engineer</option>
              <option>Manager</option>
              <option>HR</option>
              <option>Admin</option>
            </select>
          </div>
          <div className="form-group">
            <label>Project Name</label>
            <select>
              <option>Project A</option>
              <option>Project B</option>
              <option>Project C</option>
            </select>
          </div>
          <div className="form-group">
            <label>Reporting Manager</label>
            <select>
              <option>Manager X</option>
              <option>Manager Y</option>
              <option>Manager Z</option>
            </select>
          </div>
          <div className="form-group">
            <label>Contact Number</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Date of Joining</label>
            <input type="date" />
          </div>
          <div className="form-group">
            <label>Upload Photo</label>
            <input type="file" />
          </div>
        </div>
      ) : (
        <div className="form">
          <div className="form-group">
            <label>First Name</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Last Name</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Date of Birth</label>
            <input type="date" />
          </div>
          <div className="form-group">
            <label>Blood Group</label>
            <select>
              <option>A+</option>
              <option>A-</option>
              <option>B+</option>
              <option>B-</option>
              <option>O+</option>
              <option>O-</option>
              <option>AB+</option>
              <option>AB-</option>
            </select>
          </div>
          <div className="form-group">
            <label>Phone Number</label>
            <input type="text" />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" />
          </div>
          <div className="form-group">
            <label>Marital Status</label>
            <select>
              <option>Single</option>
              <option>Married</option>
              <option>Divorced</option>
              <option>Widowed</option>
            </select>
          </div>
          <div className="form-group">
            <label>Address</label>
            <textarea rows="3"></textarea>
          </div>
          <div className="form-group">
            <label>Country</label>
            <select value={"Select Country"}

            >

            </select>
          </div>
          <div className="form-group">
            <label>State</label>
            <select value="Select State">

            </select>
          </div>
          <div className="form-group">
            <label>ZIP Code</label>
            <input type="text" />
          </div>

        </div>
      )}

      {/* Buttons */}
      <div className="button-container">
        <button className="button secondary">Edit</button>
        <button className="button">Save & Continue</button>
      </div>
    </div>
  );
};

export default EmployeeDetails;
