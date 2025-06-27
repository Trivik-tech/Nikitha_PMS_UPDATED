import React, { useState } from 'react';
import { FaUser } from 'react-icons/fa';
import logo from '../../assets/images/nikithas-logo.png';
import './Forgot.css';
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

  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setEmail(e.target.value);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  console.log(email);

  if (!email.trim()) {
    setModalTitle('Error');
    setModalMessage('Email is required.');
    setShowModal(true);
    return;
  }

  setLoading(true);

  try {
    const response = await axios.post(`http://localhost:8080/api/v1/pms/auth/generate?email=${email}`,{},{withCredentials:true});
    console.log(response.data);

    setModalTitle('Success');
    setModalMessage(response.data || 'OTP has been sent to your email.');
    navigate("/reset-password");

  } catch (error) {
    setModalTitle('Error');
    setModalMessage(error.response?.data?.message || 'Something went wrong while sending OTP.');
  } finally {
    setLoading(false);
    setShowModal(true);
  }
};


  return (
    <div className="forgot-password-container">
      {loading && <Loader />}
      {showModal && <Modal message={modalMessage} closeModal={closeModal} title={modalTitle} />}

      <div className="forgot-password-form-section">
        <div className="forgot-password-logo">
          <img src={logo} alt="Logo" />
        </div>

        <h1>PMS</h1>
        <h3>Forgot Password</h3>

        <form onSubmit={handleSubmit}>
          <div className="forgot-password-input-group">
            <label htmlFor="email">Email</label>
            <div className="forgot-password-input-wrapper">
              <FaUser className="forgot-password-input-icon" />
              <input
                id="email"
                type="email"
                name="email"
                placeholder="Enter your email"
                value={email}
                onChange={handleInputChange}
              />
            </div>
          </div>

          <button type="submit" className="forgot-password-button">Submit</button>
        </form>
      </div>

      <div className="forgot-password-image-section"></div>
    </div>
  );
};

export default Forgot;
