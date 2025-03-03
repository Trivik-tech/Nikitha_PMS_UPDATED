
import Login from './components/login-component/Login';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import HrDashboard from './components/hr/HrDashboard';
import EmployeeDashboard from './components/employee/EmployeeDashboard';
import ManagerDashboard from './components/manager/dashboard/ManagerDashboard';
import Registration from './components/registration/Registration';

import Team from './components/manager/team/Team';

import ManagerProfile from './components/manager/profile/ManagerProfile';
import PerformanceReview from './components/manager/review/PerformanceReview';
import Complete from './components/manager/status/complete/Complete';
import Pending from './components/manager/status/pending/Pending';





function App() {
  return (
    <div className="App">
       <Router>
        <Routes>
          <Route exact path="/" element={<Login/>} />
          <Route exact path="/manager-dashboard" element={<ManagerDashboard/>} />
          <Route exact path="/hr-dashboard" element={<HrDashboard/>} />
          <Route exact path="/employee-dashboard" element={<EmployeeDashboard/>} />
          <Route exact path="/signup" element={<Registration/>} />

          <Route exact path="/my-team/:managerid" element={<Team/>} />

          <Route exact path="/manager-profile" element={<ManagerProfile/>} />
          <Route exact path="/manager-review" element={<PerformanceReview/>} />
          <Route exact path="/completed-assessments" element={<Complete/>} />
          <Route exact path="/pending-assessments" element={<Pending/>} />


          
          
          
          
          
          

        </Routes>
      
      </Router>
      
      
    </div>
  );
}

export default App;
