import React, { useState } from 'react';
import { FaUser } from 'react-icons/fa';
import logo from '../../assets/images/nikithas-logo.png';
import './Forgot.css';
import './ForgotResponsive.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from '../modal/Modal';
import Loader from '../modal/loader/Loader';


const Forgot = () => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [modalTitle, setModalTitle] = useState('');

  let navigate = useNavigate();

  const handleInputChange = (e) => {  
    setEmail(e.target.value);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!email) {
      setModalTitle('Error');
      setModalMessage('Email is required.');
      setShowModal(true);
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post('http://localhost:8080/api/forgot-password', {
        email: email,
      });

      setModalTitle('Success');
      setModalMessage(response.data.message || 'Password reset link sent to your email.');
    } catch (error) {
      setModalTitle('Error');
      setModalMessage(error.response?.data?.message || 'Something went wrong.');
    } finally {
      setLoading(false);
      setShowModal(true);
    }
  };

  return (
    <div className="forgot-password-container">
      {/* {loading && <Loader />} */}
      {showModal && <Modal message={modalMessage} closeModal={closeModal} title={modalTitle} />}

      <div className="forgot-password-form-section">
        <div className="forgot-password-logo">
          <img src={logo} alt="Logo" />
        </div>

        <h1>PMS</h1>
        <h3>Forgot password</h3>

        <form onSubmit={handleSubmit}>
          <div className="forgot-password-input-group">
            <label htmlFor="username">Email</label>
            <div className="forgot-password-input-wrapper">
              <FaUser className="forgot-password-input-icon" />
              <input
                id="username"
                type="text"
                name="email"
                placeholder="Enter your email"
                value={email}
                onChange={handleInputChange}
              />
            </div>
          </div>

          <button type="submit" className="forgot-password-button" onClick={()=>navigate("/reset-password")}>Submit</button>
        </form>
      </div>

      <div className="forgot-password-image-section"></div>
    </div>
  );
};

export default Forgot;
