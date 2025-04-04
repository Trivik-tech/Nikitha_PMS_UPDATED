
import Login from './components/login-component/Login';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';


import HrDashboard from './components/hr/hr-dashboard/HrDashboard';
import EmployeeDashboard from './components/employee/EmployeeDashboard';
import ManagerDashboard from './components/manager/dashboard/ManagerDashboard';
import Registration from './components/registration/Registration';

import Team from './components/manager/team/Team';

import ManagerProfile from './components/manager/profile/ManagerProfile';
import PerformanceReview from './components/manager/review/PerformanceReview';
import Complete from './components/manager/status/complete/Complete';
import Pending from './components/manager/status/pending/Pending';

import EmployeeDetails from './components/hr/empdetails/EmployeeDetails';

import HrProfile from './components/hr/profile/HrProfile';
import StartPms from './components/hr/pms/StartPms';
import Approve from './components/manager/approve-pms/Approve';


import PendingList from './components/hr/status/pending/PendingList';
import CompletedList from './components/hr/status/complete/CompletedList';
import EmployeeList from './components/hr/employee-list/EmployeeList';








function App() {
  return (
    <div className="App">
       <Router>
        <Routes>
          <Route exact path="/" element={<Login/>} />
          <Route exact path="/manager-dashboard" element={<ManagerDashboard/>} />
          
          <Route exact path="/employee-dashboard" element={<EmployeeDashboard/>} />
          <Route exact path="/signup" element={<Registration/>} />

          <Route exact path="/my-team/:managerid" element={<Team/>} />

          <Route exact path="/manager-profile" element={<ManagerProfile/>} />
          <Route exact path="/manager-review" element={<PerformanceReview/>} />
          <Route exact path="/completed-assessments" element={<Complete/>} />
          <Route exact path="/pending-assessments" element={<Pending/>} />
          

          <Route exact path="/add-employee" element={<EmployeeDetails/>} />

          <Route exact path="/hr-profile" element={<HrProfile/>} />
          <Route exact path="/hr-startpms" element={<StartPms/>} />
          <Route exact path="/employee-list" element={<EmployeeList/>} />

          
          

          <Route exact path="/hr-dashboard"  element={<HrDashboard/>}/>
          <Route exact path="/hr/pending-assessments"  element={<PendingList/>}/>
          <Route exact path="/hr/completed-assessments"  element={<CompletedList/>}/>


          <Route exact path="/approve-pms" element={<Approve/>} />



          
          
          
          
          
          

        </Routes>
      
      </Router>
      
      
    </div>
  );
}

export default App;
