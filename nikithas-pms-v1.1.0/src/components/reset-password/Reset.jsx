import React, { useState, useEffect } from 'react';
import { FaUser, FaLock } from 'react-icons/fa';
import logo from '../../assets/images/nikithas-logo.png';
import './Reset.css';
import './ResetResponsive.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from '../modal/Modal';
import Loader from '../modal/loader/Loader';
import { baseUrl } from '../urls/CommenUrl';

const Reset = () => {
  const [otp, setOtp] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [title, setTitle] = useState('');
  const [timeLeft, setTimeLeft] = useState(600); // 10 minutes

  const navigate = useNavigate(); 

  // OTP Countdown Timer
  useEffect(() => {
    if (timeLeft <= 0) return;
    const timer = setInterval(() => setTimeLeft(prev => prev - 1), 1000);
    return () => clearInterval(timer);
  }, [timeLeft]);

  const formatTime = (seconds) => {
    const min = Math.floor(seconds / 60);
    const sec = seconds % 60;
    return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
  };

  const handleResetSubmit = async (e) => {
    e.preventDefault();

    if (!otp.trim() || !password.trim()) {
      setErrorMessage('OTP and new password are required.');
      setTitle('Validation Error');
      setShowModal(true);
      return;
    }

    if (timeLeft <= 0) {
      setErrorMessage('OTP has expired. Please request a new one.');
      setTitle('OTP Expired');
      setShowModal(true);
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(
        `${baseUrl}/api/v1/pms/auth/validate-reset?otp=${otp}`,
        { password },{withCredentials:true}
      );
      setTitle('Password Reset Successfully');
      setPassword(response.data.message || 'Password reset successfully.');
      setShowModal(true);
      // Optional: Redirect to login page after 2s
      setTimeout(() => navigate('/'), 2000);
    } catch (error) {
      if (error.response) {
        setErrorMessage(error.response.data.message || 'Invalid OTP or password.');
        setTitle('Reset Failed');
      } else {
        setErrorMessage('Server error. Please try again later.');
        setTitle('Error');
      }
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const closeModal = () => setShowModal(false);

  return (
    <div className="reset-password-container">
      {loading && <Loader />}
      {showModal && <Modal message={errorMessage} closeModal={closeModal} title={title} />}

      <div className="reset-password-form-section">
        <div className="reset-password-logo">
          <img src={logo} alt="Logo" />
        </div>

        <h1>PMS</h1>
        <h3>Reset Password</h3>

        <form onSubmit={handleResetSubmit}>
          <div className="reset-password-input-group">
            <label htmlFor="otp">OTP</label>
            <div className="reset-password-input-wrapper">
              <FaUser className="reset-password-input-icon" />
              <input
                id="otp"
                type="text"
                name="otp"
                placeholder="Enter OTP"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
              />
            </div>
            <div className="otp-timer">
              {timeLeft > 0 ? (
                <span>OTP valid for: {formatTime(timeLeft)}</span>
              ) : (
                <span style={{ color: 'red' }}>OTP expired</span>
              )}
            </div>
          </div>

          <div className="reset-password-input-group">
            <label htmlFor="password">New Password</label>
            <div className="reset-password-input-wrapper">
              <FaLock className="reset-password-input-icon" />
              <input
                id="password"
                type="password"
                name="password"
                placeholder="Enter new password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <button type="submit" className="reset-password-button">Reset</button>
        </form>
      </div>

      <div className="reset-password-image-section"></div>
    </div>
  );
};

export default Reset;
