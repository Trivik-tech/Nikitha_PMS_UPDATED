import React, { useState, useEffect } from 'react';
import { FaUser, FaLock } from 'react-icons/fa';
import logo from '../../assets/images/nikithas-logo.png';
import './Reset.css';
import './ResetResponsive.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from '../modal/Modal';
import { Link } from 'react-router-dom';
import Loader from '../modal/loader/Loader';

const Reset = () => {
  const [login, setLogin] = useState({ username: '', password: '' });
  const [errorMessage, setErrorMessage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [title, setTitle] = useState('');
  const [timeLeft, setTimeLeft] = useState(600); // 600 seconds = 10 minutes

  const navigation = useNavigate();
  const { username, password } = login;

  const onInputChange = (e) => {
    setLogin({ ...login, [e.target.name]: e.target.value });
  };

  // Timer countdown
  useEffect(() => {
    if (timeLeft <= 0) return;

    const timer = setInterval(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft]);

  const formatTime = (seconds) => {
    const min = Math.floor(seconds / 60);
    const sec = seconds % 60;
    return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
  };

  const handleHardcodedLogin = (e) => {
    e.preventDefault();

    if (!username.trim() && !password.trim()) {
      setErrorMessage('Username and password are required.');
      setTitle('Login Error');
      setShowModal(true);
      return;
    }

    if (!username.trim()) {
      setErrorMessage('Username is required.');
      setTitle('Login Error');
      setShowModal(true);
      return;
    }

    if (!password.trim()) {
      setErrorMessage('Password is required.');
      setTitle('Login Error');
      setShowModal(true);
      return;
    }

    if (timeLeft <= 0) {
      setErrorMessage('OTP has expired. Please request a new one.');
      setTitle('OTP Expired');
      setShowModal(true);
      return;
    }

    if (username === 'MG1234' && password === '12345') {
      navigation('/manager-dashboard');
    } else if (username === 'HR1234' && password === '12345') {
      navigation('/hr-dashboard');
    } else if (username === 'EMP1234' && password === '12345') {
      navigation('/employee-dashboard');
    } else {
      setErrorMessage('Invalid credentials. Please try again.');
      setTitle('Login Error');
      setShowModal(true);
    }
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage('');
    setTitle('');
    setShowModal(false);
    setLoading(true);

    try {
      const response = await axios.post('http://localhost:8080/api/v1/pms/auth/login', login);
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        await navigateTo(response.data.token);
      }
    } catch (error) {
      if (error.response) {
        setErrorMessage('Invalid credentials. Please try again.');
        setTitle('Login Error');
      } else if (error.request) {
        setErrorMessage('Unable to connect to the server. Please check if the backend is running.');
        setTitle('Server Error');
      } else {
        setErrorMessage('An unexpected error occurred. Please try again later.');
      }
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const navigateTo = async (token) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/pms/auth/${token}`);
      if (response.data.role === 'MANAGER') {
        navigation('/manager-dashboard');
      } else if (response.data.role === 'HR') {
        navigation('/hr-dashboard');
      } else if (response.data.role === 'EMPLOYEE') {
        navigation('/employee-dashboard');
      }
    } catch (error) {
      console.error('Error navigating:', error);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };

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

        <form onSubmit={handleHardcodedLogin}>
          <div className="reset-password-input-group">
            <label htmlFor="username">OTP</label>
            <div className="reset-password-input-wrapper">
              <FaUser className="reset-password-input-icon" />
              <input
                id="username"
                type="text"
                name="username"
                placeholder="Enter OTP"
                value={username}
                onChange={onInputChange}
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
                placeholder="Enter New password"
                value={password}
                onChange={onInputChange}
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
 