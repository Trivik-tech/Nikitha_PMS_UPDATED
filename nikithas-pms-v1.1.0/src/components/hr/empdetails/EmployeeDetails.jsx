import { useEffect, useState } from "react";
import { FaHome } from "react-icons/fa";
import "./EmployeeDetails.css";
import "./ResponsiveEmpDetails.css";
import logo from "../../../assets/images/nikithas-logo.png";
import FileConfirmation from "../../modal/file-confirmation/FileConfirmation";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import Loader from "../../modal/loader/Loader";
import Modal from "../../modal/Modal";
import { baseUrl } from "../../urls/CommenUrl";

const EmployeeDetails = () => {
  const [payload, setPayload] = useState({
    empId: "",
    name: "",
    dob: "",
    dateOfJoining: "",
    currentDesignation: "",
    department: "",
    branch: "",
    category: "",
    lastWorkingDate: "",
    officialEmailId: "",
    mobileNumber: "",
    reportingManager: "",
    emailId: "",
    role: ""
  });

  const [file, setFile] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [statusModal, setStatusModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalTitle, setModalTitle] = useState("");
  const [departmentList, setDepartmentsList] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    loadDepartments();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPayload((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const uploadData = () => {
    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.style.display = "none";

    fileInput.addEventListener("change", (event) => {
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
      const result=await axios.post(`${baseUrl}/api/v1/pms/hr/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      console.log(result.data)

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

  const handleSaveEmployee = async () => {
    try {
      setLoading(true);

      if (!payload.role || payload.role === "Select Role") {
        setModalTitle("Validation Error");
        setModalMessage("Please select a valid role before saving.");
        setStatusModal(true);
        setLoading(false);
        return;
      }

      const updatedEmployee={
        ...payload,
        role: payload.role?.toUpperCase() || "",
        department: payload.department?.toString() || "" // 
      }

      const result = await axios.post(`${baseUrl}/api/v1/pms/hr/register-employee`, updatedEmployee);
      console.log(result.data);
      setModalTitle("Save Successful");
      setModalMessage("Employee data saved successfully!");
      setStatusModal(true);

      setTimeout(() => {
        navigate("/employee-list");
      }, 2000);
    } catch (error) {
      console.error("Save failed:", error);
      setModalTitle("Save Failed");
      setModalMessage("Error saving employee data.");
      setStatusModal(true);
    } finally {
      setLoading(false);
    }
  };

  const loadDepartments = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-departments`);
      setDepartmentsList(result.data.departments || []);
    } catch (error) {
      console.error("Error loading departments:", error);
    }
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

      <div className="hr-module-add-employee-form">
        <div className="hr-module-add-employee-form-group">
          <label>Employee ID</label>
          <input type="text" name="empId" value={payload.empId} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Name</label>
          <input type="text" name="name" value={payload.name} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Date of Birth</label>
          <input type="date" name="dob" value={payload.dob} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Date of Joining</label>
          <input type="date" name="dateOfJoining" value={payload.dateOfJoining} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Current Designation</label>
          <input type="text" name="currentDesignation" value={payload.currentDesignation} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Department</label>
          <select name="department" value={payload.department} onChange={handleChange}>
            <option value="">Select Department</option>
            {departmentList.map((dept, index) => (
              <option key={index} value={dept}>{dept}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Branch</label>
          <input type="text" name="branch" value={payload.branch} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Category</label>
          <select name="category" value={payload.category} onChange={handleChange}>
            <option value="">Select Category</option>
            {["Manager", "Executive", "Worker", "Driver", "Others", "HR"].map((category, index) => (
              <option key={index} value={category}>{category}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Last Working Date</label>
          <input type="date" name="lastWorkingDate" value={payload.lastWorkingDate} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Official Email ID</label>
          <input type="email" name="officialEmailId" value={payload.officialEmailId} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Mobile Number</label>
          <input type="text" name="mobileNumber" value={payload.mobileNumber} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Reporting Manager</label>
          <input type="text" name="reportingManager" value={payload.reportingManager} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Personal Email</label>
          <input type="email" name="emailId" value={payload.emailId} onChange={handleChange} />
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Role</label>
          <select name="role" value={payload.role} onChange={handleChange}>
            <option value="">Select Role</option>
            {["Employee", "HR", "Manager"].map((role, index) => (
              <option key={index} value={role}>{role}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-add-employee-form-group">
          <label>Upload Photo</label>
          <input type="file" disabled />
        </div>
      </div>

      <div className="hr-module-add-employee-button-container">
        <button onClick={uploadData} className="hr-module-add-employee-button primary">
          Upload
        </button>
        <button onClick={handleSaveEmployee} className="hr-module-add-employee-button">
          Save
        </button>
      </div>
    </div>
  );
};

export default EmployeeDetails;
