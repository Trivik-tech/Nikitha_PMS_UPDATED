import React, { useEffect, useState } from "react";
import "./Approve.css";
import logo from "../../../assets/images/nikithas-logo.png"; // Make sure the path is correct
import { FaHome } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import Modal from "../../../components/modal/Modal"; // Ensure the correct path
import axios from "axios";

const Approve = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [title, setTitle] = useState("");
  const [krakpi,setKraKpis]=useState([]);
 const [name,setName]=useState("")
 const [designation,setDesignation]=useState("")
 const [department,setDepartment]=useState("")
 const [manager,setManager]=useState("")

  const emplId=useParams();

  
useEffect(()=>{
  const loadKraKpis= async()=>{

    try{
      console.log(emplId)

      const result =await axios.get(`http://localhost:8080/api/v1/pms/manager/kra-kpi/Pradeep Prahalada Rao Kubair/${emplId.id}`)
      console.log(result.data)
      setKraKpis(result.data.kra)
      setName(result.data.employee.name)
      setDesignation(result.data.employee.currentDesignation)
      setDepartment(result.data.employee.department)
      setManager(result.data.employee.reportingManager)
    
    }catch(error){
      console.error(error)
    }

  }

  loadKraKpis()
},[])
  

  const handleApproveClick = () => {
    setErrorMessage("PMS has been approved successfully.");
    setTitle("PMS Approve");
    setShowModal(true);
    approvekrakpi() // Show modal when "Approve" is clicked
  };

  const handleRejectClick = () => {
    setErrorMessage("PMS has been rejected successfully.");
    setTitle("PMS Reject");
    setShowModal(true);
    rejectKraKpi() // Show modal when "Reject" is clicked
  };

  const closeModal = () => {
    setShowModal(false); // Close modal
  };

  const approvekrakpi= async()=>{
    const approve = {
      approveStatus: true
    }

    try{
 

     const result = await axios.patch(`http://localhost:8080/api/v1/pms/manager/approve-krakpi/${emplId.id}/Pradeep Prahalada Rao Kubair`, approve)
    console.log(result.data)
    }
    catch(error){

      console.error(error)
    }
  }
  const rejectKraKpi = async ()=>{
    const approve = {
      approveStatus: false
    }

    try{
 

     const result = await axios.patch(`http://localhost:8080/api/v1/pms/manager/approve-krakpi/${emplId.id}/Pradeep Prahalada Rao Kubair`, approve)
    console.log(result.data)
    }
    catch(error){

      console.error(error)
    }
  }
  return (
    <div className="manager-approve-container">
      {showModal && (
        <Modal message={errorMessage} closeModal={closeModal} title={title} />
      )}
      <header className="manager-approve-header">
        <div className="manager-approve-title-section">
          <Link to="/manager-dashboard" className="icon-link">
            <FaHome className="icon home-icon" />
          </Link>
          <h1 className="manager-approve-title">Performance Approval</h1>
          <img src={logo} alt="Company Logo" className="manager-approve-logo" />
        </div>

        <div className="manager-approve-filters">
          <label>Employee ID: <input type="text" value={emplId.id} readOnly /></label>
          <label>Employee Name: <input type="text" value={name} readOnly /></label>
          <label>Designation: <input type="text" value={designation} readOnly /></label>
          <label>Department: <input type="text" value={department} readOnly /></label>
          <label>Manager: <input type="text" value={manager} readOnly /></label>
        </div>
      </header>

      <div className="manager-approve-sections">
        {krakpi.map((data, index) => (
          <div key={index} className="manager-approve-section">
            <div className="manager-approve-section-header">
              <h3>KRA - {data.kraName}</h3>
              <span>Weightage: {data.weightage}</span>
            </div>
            <table className="manager-approve-table">
              <thead>
                <tr>
                  <th>KPI's</th>
                  <th>Weightage</th>
                </tr>
              </thead>
              <tbody>
                {data.kpi.map((item, idx) => (
                  <tr key={idx}>
                    <td>{item.description}</td>
                    <td>{item.weightage}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>

      <div className="manager-approve-actions">
        <button className="manager-approve-submit-btn approve-btn" onClick={handleApproveClick}>
          Approve
        </button>
        <button className="manager-approve-submit-btn reject-btn" onClick={handleRejectClick}>
          Reject
        </button>
      </div>
    </div>
  );
};

export default Approve;
