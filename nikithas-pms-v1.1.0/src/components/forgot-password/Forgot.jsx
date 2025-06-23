import React, { useState } from 'react';
import { FaUser, FaLock } from 'react-icons/fa';
import logo from '../../assets/images/nikithas-logo.png';
import './Forgot.css';
import './ForgotResponsive.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Modal from '../modal/Modal'; 
import { Link } from 'react-router-dom';
import Loader from '../modal/loader/Loader';

const Forgot = () => {
//   const [login, setLogin] = useState({ email: '', password: '' });
//   const [errorMessage, setErrorMessage] = useState('');
//   const [showModal, setShowModal] = useState(false);
//   const [loading, setLoading] = useState(false);
//   const [title, setTitle] = useState('');

//   const navigation = useNavigate();
//   const { email, password } = submit;

  // const onInputChange = (e) => {  
  //   setLogin({ ...login, [e.target.name]: e.target.value });
  // };

  // const handleHardcodedLogin = (e) => {
  //   e.preventDefault();
    
  //   if (!username.trim() && !password.trim()) {
  //     setErrorMessage('Username and password are required.');
  //     setTitle('Login Error');
  //     setShowModal(true);
  //     return;
  //   }

  //   if (!username.trim()) {
  //     setErrorMessage('Username is required.');
  //     setTitle('Login Error');
  //     setShowModal(true);
  //     return;
  //   }

  //   if (!password.trim()) {
  //     setErrorMessage('Password is required.');
  //     setTitle('Login Error');
  //     setShowModal(true);
  //     return;
  //   }

  //   if (username === 'MG1234' && password === '12345') {
  //     navigation('/manager-dashboard');
  //   } else if (username === 'HR1234' && password === '12345') {
  //     navigation('/hr-dashboard');
  //   } else if (username === 'EMP1234' && password === '12345') {
  //     navigation('/employee-dashboard');
  //   } else {
  //     setErrorMessage('Invalid credentials. Please try again.');
  //     setTitle('Login Error');
  //     setShowModal(true);
  //   }
  // };

  // const onSubmit = async (e) => {
  //   e.preventDefault();
  //   setErrorMessage('');
  //   setTitle('');
  //   setShowModal(false);
  //   setLoading(true);

  //   try {
  //     const response = await axios.post('http://localhost:8080/api/v1/pms/auth/login', login);
  //     if (response.data.token) {
  //       localStorage.setItem('token', response.data.token);
  //       await navigateTo(response.data.token);
  //     }
  //   } catch (error) {
  //     if (error.response) {
  //       setErrorMessage('Invalid credentials. Please try again.');
  //       setTitle('Login Error');
  //     } else if (error.request) {
  //       setErrorMessage('Unable to connect to the server. Please check if the backend is running.');
  //       setTitle('Server Error');
  //     } else {
  //       setErrorMessage('An unexpected error occurred. Please try again later.');
  //     }
  //     setShowModal(true);
  //   } finally {
  //     setLoading(false);
  //   }
  // };

  // const navigateTo = async (token) => {
  //   try {
  //     const response = await axios.get(`http://localhost:8080/api/v1/pms/auth/${token}`);
  //     if (response.data.role === 'MANAGER') {
  //       navigation('/manager-dashboard');
  //     } else if (response.data.role === 'HR') {
  //       navigation('/hr-dashboard');
  //     } else if (response.data.role === 'EMPLOYEE') {
  //       navigation('/employee-dashboard');
  //     }
  //   } catch (error) {
  //     console.error('Error navigating:', error);
  //   }
  // };

  // const closeModal = () => {
  //   setShowModal(false);
  // };

  return (
    <div className="forgot-password-container">
      {/* {loading && <Loader />}
      {showModal && <Modal message={errorMessage} closeModal={closeModal} title={title} />} */}

      <div className="forgot-password-form-section">
        <div className="forgot-password-logo">
          <img src={logo} alt="Logo" />
        </div>

        <h1>PMS</h1>
        <h3>Forgot password</h3>

        <form >
          <div className="forgot-password-input-group">
            <label htmlFor="username">Email</label>
            <div className="forgot-password-input-wrapper">
              <FaUser className="forgot-password-input-icon" />
              <input
                id="username"
                type="text"
                name="email"
                placeholder="Enter your email"
                // value={email}
                // onChange={onInputChange}
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


export default  Forgot;
