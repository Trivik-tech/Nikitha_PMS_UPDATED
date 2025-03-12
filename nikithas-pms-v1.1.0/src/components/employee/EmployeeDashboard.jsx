import {React,useState,useEffect} from 'react'
import Modal from '../modal/Modal';
import { useNavigate } from 'react-router-dom';

const EmployeeDashboard = () => {

  const [errorMessage, setErrorMessage] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [title, setTitle] = useState('');
    const navigation=useNavigate();

    useEffect(() => {

      setErrorMessage('Employee Modul is under progress.');
      setTitle('Employee Module Progress');
      setShowModal(true);
        
      }, []);
      const closeModal = () => {
        setShowModal(false);
        navigation("/")
      };

  return (
    <div>
      {showModal && <Modal message={errorMessage} closeModal={closeModal} title={title} />}
      
    </div>
  )
}

export default EmployeeDashboard
