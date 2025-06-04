import { useState } from "react";
import { FaHome } from "react-icons/fa";
import "./EmployeeDetails.css";
import logo from '../../../assets/images/nikithas-logo.png';
import FileConfirmation from "../../modal/file-confirmation/FileConfirmation";
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';
import Loader from '../../modal/loader/Loader';
import Modal from '../../modal/Modal';
import '../../urls/CommenUrl'
import { baseUrl } from "../../urls/CommenUrl";


const EmployeeDetails = () => {
  const [activeTab, setActiveTab] = useState("employment");
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

  const handleConfirmUpload = async () => {
    setShowModal(false);

    if (!file) {
      setModalTitle("Upload Failed");
      setModalMessage("No file selected!");
      setStatusModal(true);
      return;
    }

    setLoading(true);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        `${baseUrl}/api/v1/pms/hr/upload`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        }
      );

      setModalTitle("Upload Successful");
      setModalMessage("File uploaded successfully!");
      setStatusModal(true);

      setTimeout(() => {
        navigate("/employee-list");
      }, 2000);
    } catch (error) {
      console.error("Upload failed:", error);
      setModalTitle("Upload Failed");
      setModalMessage("File upload failed. Please try again.");
      setStatusModal(true);
    } finally {
      setLoading(false);
      setFile(null);
    }
  };

  const handleCancelUpload = () => {
    setShowModal(false);
    setFile(null);
  };

  const handleSaveAndContinue = () => {
    setActiveTab("personal");
  };

  return (
    <div className="hr-module-add-employee-container">
      {showModal && (
        <FileConfirmation
          file={file}
          onConfirm={handleConfirmUpload}
          onCancel={handleCancelUpload}
        />
      )}

      {loading && <Loader />}

      {statusModal && (
        <Modal
          title={modalTitle}
          message={modalMessage}
          closeModal={() => setStatusModal(false)}
        />
      )}

      <div className="hr-module-add-employee-top-bar">
        <h2 className="hr-module-add-employee-header">
          <Link to="/hr-dashboard" className="hr-module-add-employee-home-icon">
            <FaHome className="employee-list-home-icon" />
          </Link>
          Employee Registration
        </h2>
        <img src={logo} className="hr-module-add-employee-company-icon" alt="Company Logo" />
      </div>

      <div className="hr-module-add-employee-tabs">
        <button
          className={`hr-module-add-employee-tab ${activeTab === "employment" ? "active" : ""}`}
          onClick={() => setActiveTab("employment")}
        >
          Employment Information
        </button>
        <button
          className={`hr-module-add-employee-tab ${activeTab === "personal" ? "active" : ""}`}
          onClick={() => setActiveTab("personal")}
        >
          Personal Information
        </button>
      </div>

      {activeTab === "employment" ? (
        <div className="hr-module-add-employee-form">
          <div className="hr-module-add-employee-form-group">
            <label>Employee ID</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Designation</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>First Name</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Last Name</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Role</label>
            <select>
              <option>Developer</option>
              <option>Team Lead</option>
              <option>Project Manager</option>
              <option>QA Engineer</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Department</label>
            <select>
              <option>Engineer</option>
              <option>Manager</option>
              <option>HR</option>
              <option>Admin</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Project Name</label>
            <select>
              <option>Project A</option>
              <option>Project B</option>
              <option>Project C</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Reporting Manager</label>
            <select>
              <option>Manager X</option>
              <option>Manager Y</option>
              <option>Manager Z</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Contact Number</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Date of Joining</label>
            <input type="date" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Upload Photo</label>
            <input type="file" />
          </div>
        </div>
      ) : (
        <div className="hr-module-add-employee-form">
          <div className="hr-module-add-employee-form-group">
            <label>First Name</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Last Name</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Date of Birth</label>
            <input type="date" />
          </div>
          <div className="hr-module-add-employee-form-group">
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
          <div className="hr-module-add-employee-form-group">
            <label>Phone Number</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Email</label>
            <input type="email" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Marital Status</label>
            <select>
              <option>Single</option>
              <option>Married</option>
              <option>Divorced</option>
              <option>Widowed</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Country</label>
            <select>
              <option>India</option>
              <option>USA</option>
              <option>Canada</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>State</label>
            <select>
              <option>California</option>
              <option>Texas</option>
              <option>New York</option>
            </select>
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>ZIP Code</label>
            <input type="text" />
          </div>
          <div className="hr-module-add-employee-form-group">
            <label>Address</label>
            <textarea rows="2"></textarea>
          </div>
        </div>
      )}

      <div className="hr-module-add-employee-button-container">
        <button onClick={uploadData} className="hr-module-add-employee-button primary">Upload</button>
        <button onClick={handleSaveAndContinue} className="hr-module-add-employee-button">Save & Continue</button>
      </div>
    </div>
  );
};

export default EmployeeDetails;
