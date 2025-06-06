import { useState } from "react";
import { FaHome } from "react-icons/fa";
import "./EmployeeUpdate.css";
import "./ResponsiveEmpUpdate.css";
import logo from '../../../assets/images/nikithas-logo.png';
import FileConfirmation from "../../modal/file-confirmation/FileConfirmation";
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';
import Loader from '../../modal/loader/Loader';
import Modal from '../../modal/Modal';
import { baseUrl } from "../../urls/CommenUrl";

const UpdateEmployee = () => {
  const [file, setFile] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [statusModal, setStatusModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalTitle, setModalTitle] = useState("");

  const navigate = useNavigate();

  const uploadData = () => {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.style.display = 'none';

    fileInput.addEventListener('change', (event) => {
      const selectedFile = event.target.files[0];
      if (selectedFile) {
        setFile(selectedFile);
        setShowModal(true);
      }
    });

    document.body.appendChild(fileInput);
    fileInput.click();
    fileInput.remove();
  };

  

  return (
    <div className="hr-module-update-employee-container">
      

      {loading && <Loader />}

      {statusModal && (
        <Modal
          title={modalTitle}
          message={modalMessage}
          closeModal={() => setStatusModal(false)}
        />
      )}

      <div className="hr-module-update-employee-top-bar">
        <h2 className="hr-module-update-employee-header">
          <Link to="/hr-dashboard" className="hr-module-update-employee-home-icon">
            <FaHome className="employee-list-home-icon" />
          </Link>
          Employee Updation
        </h2>
        <img src={logo} className="hr-module-update-employee-company-icon" alt="Company Logo" />
      </div>

      <div className="hr-module-update-employee-form">
        <div className="hr-module-update-employee-form-group">
          <label>Employee ID</label>
          <input type="text" name="empId" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Name</label>
          <input type="text" name="name" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Date of Birth</label>
          <input type="date" name="dob" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Date of Joining</label>
          <input type="date" name="dateOfJoining" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Role</label>
          <input type="text" name="role" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Current Designation</label>
          <input type="text" name="currentDesignation" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Department</label>
          <select name="department">
            <option>Engineer</option>
            <option>Manager</option>
            <option>HR</option>
            <option>Admin</option>
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Branch</label>
          <input type="text" name="branch" style={{ minWidth: "672px" }} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Category</label>
          <select name="category">
            <option>Permanent</option>
            <option>Contract</option>
            <option>Intern</option>
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Reporting Manager</label>
          <select name="reportingManager" style={{ minWidth: "300px" }}>
            <option>Manager X</option>
            <option>Manager Y</option>
            <option>Manager Z</option>
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Contact Number</label>
          <input type="text" name="mobileNumber" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Official Email ID</label>
          <input type="email" name="officialEmailId" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Personal Email</label>
          <input type="email" name="emailId" />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Upload Photo</label>
          <input type="file" />
        </div>
      </div>

      <div className="hr-module-update-employee-button-container">
        <button className="hr-module-update-employee-button">Save</button>
      </div>
    </div>
  );
};

export default UpdateEmployee;
