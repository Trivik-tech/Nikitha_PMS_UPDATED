import { useEffect, useState } from "react";
import { FaHome } from "react-icons/fa";
import "./EmployeeUpdate.css";
import "./ResponsiveEmpUpdate.css";
import logo from '../../../assets/images/nikithas-logo.png';
import { Link, useNavigate, useParams } from "react-router-dom";
import axios from 'axios';
import Loader from '../../modal/loader/Loader';
import Modal from '../../modal/Modal';
import { baseUrl } from "../../urls/CommenUrl";
import { decrypt } from "../../utils/encryptUtils";

const UpdateEmployee = () => {
  const [employee, setEmployee] = useState({
    empId: "",
    name: "",
    dob: "",
    dateOfJoining: "",
    role: "",
    currentDesignation: "",
    department: "",
    branch: "",
    category: "",
    reportingManager: "",
    mobileNumber: "",
    officialEmailId: "",
    emailId: ""
  });

  const [departmentList, setDepartmentList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [statusModal, setStatusModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalTitle, setModalTitle] = useState("");

  const navigate = useNavigate();
  const { id: encodedId } = useParams();
  const id = decrypt(encodedId);

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().split("T")[0];
  };

  useEffect(() => {
    loadEmployee();
    loadDepartments();
  }, []);

  const loadDepartments = async () => {
    try {
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-departments`);
      setDepartmentList(result.data.departments || []);
    } catch (error) {
      console.error("Error loading departments:", error);
    }
  };

  const loadEmployee = async () => {
    try {
      setLoading(true);
      const result = await axios.get(`${baseUrl}/api/v1/pms/hr/get-employee/${id}`);
      const empData = result.data;

      empData.dob = formatDate(empData.dob);
      empData.dateOfJoining = formatDate(empData.dateOfJoining);
      setEmployee(empData);
    } catch (error) {
      console.error("Error loading employee:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEmployee((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSave = async () => {
    try {
      setLoading(true);
      const updatedEmployee = {
        ...employee,
        role: employee.role || "",
        department: employee.department?.toString() || ""
      };

      const result = await axios.put(`${baseUrl}/api/v1/pms/hr/update-employee/${id}`, updatedEmployee);
      setModalTitle("Success");
      setModalMessage("Employee updated successfully!");
      setStatusModal(true);

      setTimeout(() => {
        navigate("/employee-list");
      }, 2000);
    } catch (error) {
      console.error("Update failed:", error);
      setModalTitle("Error");
      setModalMessage("Failed to update employee.");
      setStatusModal(true);
    } finally {
      setLoading(false);
    }
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
        {/* Fields */}
        <div className="hr-module-update-employee-form-group">
          <label>Employee ID</label>
          <input type="text" name="empId" value={employee.empId} readOnly />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Name</label>
          <input type="text" name="name" value={employee.name || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Date of Birth</label>
          <input type="date" name="dob" value={employee.dob || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Date of Joining</label>
          <input type="date" name="dateOfJoining" value={employee.dateOfJoining || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Role</label>
          <select name="role" value={employee.role || ""} onChange={handleChange}>
            {["EMPLOYEE", "HR", "MANAGER"].map((role, index) => (
              <option key={index} value={role}>{role.charAt(0) + role.slice(1).toLowerCase()}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Current Designation</label>
          <input type="text" name="currentDesignation" value={employee.currentDesignation || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Department</label>
          <select name="department" value={employee.department || ""} onChange={handleChange}>
            <option value="">-- Select Department --</option>
            {departmentList.map((dept, index) => (
              <option key={index} value={dept}>{dept}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Branch</label>
          <input type="text" name="branch" value={employee.branch || ""} onChange={handleChange} style={{ minWidth: "672px" }} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Category</label>
          <select name="category" value={employee.category || ""} onChange={handleChange}>
            {["Manager", "Executive", "Worker", "Driver", "Others", "HR"].map((cat, index) => (
              <option key={index} value={cat}>{cat}</option>
            ))}
          </select>
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Reporting Manager</label>
          <input type="text" name="reportingManager" value={employee.reportingManager || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Contact Number</label>
          <input type="text" name="mobileNumber" value={employee.mobileNumber || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Official Email ID</label>
          <input type="email" name="officialEmailId" value={employee.officialEmailId || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Personal Email</label>
          <input type="email" name="emailId" value={employee.emailId || ""} onChange={handleChange} />
        </div>
        <div className="hr-module-update-employee-form-group">
          <label>Upload Photo</label>
          <input type="file" disabled />
        </div>
      </div>

      <div className="hr-module-update-employee-button-container">
        <button className="hr-module-update-employee-button" onClick={handleSave}>
          Save
        </button>
      </div>
    </div>
  );
};

export default UpdateEmployee;
