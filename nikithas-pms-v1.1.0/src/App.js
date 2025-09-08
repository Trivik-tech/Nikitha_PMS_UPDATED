import React, { useEffect, useState, createContext, useContext } from 'react';
import Login from './components/login-component/Login';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

import HrDashboard from './components/hr/hr-dashboard/HrDashboard';
import EmployeeDashboard from './components/employee/employee-dashboard/EmployeeDashboard';
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
import CompletedList  from './components/hr/status/complete/CompletedList';

import EmployeeList from './components/hr/employee-list/EmployeeList';

import Performance from './components/employee/performance/Performance';
import EmployeeReview from './components/employee/emplyoee-review/EmployeeReview';
import KrakpiRegistration from './components/employee/kpi-registration/KrakpiRegistration';
import EmployeeInfo from './components/hr/employee-info/EmployeeInfo';
import UpdateEmployee from './components/hr/updateemployee/UpdateEmployee';
import Forgot from './components/forgot-password/Forgot';
import Reset from './components/reset-password/Reset';

import { baseUrl } from './components/urls/CommenUrl';
import Loader from './components/modal/loader/Loader';
import KraKpiReport from './components/hr/kra-kpi-report/KraKpiReport';
import KraKpiList from './components/hr/krakpiemplist/KraKpiList';
import KraKpiUpload from './components/hr/krakpiupload/KraKpiUpload';
import PastEmployee from './components/hr/past-employee/PastEmployee';

const AuthContext = createContext();

function AuthProvider({ children }) {
  // Try to load role from localStorage on mount
  const [role, setRoleState] = useState(() => localStorage.getItem('role'));
  const [token, setTokenState] = useState(() => localStorage.getItem('token'));

  // Persist role and token to localStorage
  const setRole = (newRole) => {
    setRoleState(newRole);
    if (newRole) {
      localStorage.setItem('role', newRole);
    } else {
      localStorage.removeItem('role');
    }
  };

  const setToken = (newToken) => {
    setTokenState(newToken);
    if (newToken) {
      localStorage.setItem('token', newToken);
    } else {
      localStorage.removeItem('token');
    }
  };

  // Provide role, setRole, token, setToken to context
  return (
    <AuthContext.Provider value={{ role, setRole, token, setToken }}>
      {children}
    </AuthContext.Provider>
  );
}

function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}

// This handler will store the token and role in localStorage to persist after reload
function AuthHandler() {
  const navigation = useNavigate();
  const { token } = useParams();
  const { setRole, setToken } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchRole() {
      try {
        const response = await axios.get(`${baseUrl}/api/v1/pms/auth/${token}`);
        const role = response.data?.role;
        setRole(role);
        setToken(token);

        if (role === 'HR') navigation('/hr-dashboard', { replace: true });
        else if (role === 'MANAGER') navigation('/manager-dashboard', { replace: true });
        else if (role === 'EMPLOYEE') navigation('/employee-dashboard', { replace: true });
        else navigation('/', { replace: true });
      } catch (error) {
        console.error('Error fetching role:', error);
        setRole(null);
        setToken(null);
        navigation('/', { replace: true });
      } finally {
        setLoading(false);
      }
    }

    if (token) fetchRole();
    else navigation('/', { replace: true });
    // eslint-disable-next-line
  }, [token]);

  return loading ? <Loader /> : null;
}

// This checks for authentication and optionally validates the token on reload
function RequireAuth({ children, allowedRoles }) {
  const { role, token } = useAuth();
  const [loading, setLoading] = useState(false);
  const [valid, setValid] = useState(true);

  // Validate the token on mount (only if role exists but token might be expired or invalid)
  useEffect(() => {
    async function validateToken() {
      if (!token || !role) {
        setValid(false);
        return;
      }
      setLoading(true);
      try {
        // Validate token using your backend endpoint
        const response = await axios.get(`${baseUrl}/api/v1/pms/auth/${token}`);
        const serverRole = response.data?.role;
        if (serverRole !== role || !allowedRoles.includes(role)) {
          setValid(false);
        } else {
          setValid(true);
        }
      } catch (err) {
        setValid(false);
      } finally {
        setLoading(false);
      }
    }

    // Only validate if token or role changes
    validateToken();
    // eslint-disable-next-line
  }, [token, role]);

  if (loading) return <Loader />;
  if (!role || !token || !valid || !allowedRoles.includes(role)) return <Navigate to="/" replace />;
  return children;
}

function App() {
  return (
    <div className="App">
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/signup" element={<Registration />} />
            <Route path="/forgot-password" element={<Forgot />} />
            <Route path="/reset-password" element={<Reset />} />
            <Route path="/auth/:token" element={<AuthHandler />} />

            {/* HR Routes */}
            <Route path="/hr-dashboard" element={<HrDashboard />} />
            {/* <Route path="/hr-dashboard" element={<RequireAuth allowedRoles={['HR']}><HrDashboard /></RequireAuth>} /> */}
            <Route path="/add-employee" element={<RequireAuth allowedRoles={['HR']}><EmployeeDetails /></RequireAuth>} />
            <Route path="/hr-profile" element={<RequireAuth allowedRoles={['HR']}><HrProfile /></RequireAuth>} />
            <Route path="/hr-startpms" element={<RequireAuth allowedRoles={['HR']}><StartPms /></RequireAuth>} />
            <Route path="/employee-list" element={<RequireAuth allowedRoles={['HR']}><EmployeeList /></RequireAuth>} />
            <Route path="/hr/pending-assessments" element={<RequireAuth allowedRoles={['HR']}><PendingList /></RequireAuth>} />
            <Route path="/hr/completed-assessments" element={<RequireAuth allowedRoles={['HR']}><CompletedList/></RequireAuth>} />
            <Route path="/employee-info/:id" element={<RequireAuth allowedRoles={['HR']}><EmployeeInfo /></RequireAuth>} />
            <Route path="/update-employee/:id" element={<RequireAuth allowedRoles={['HR']}><UpdateEmployee /></RequireAuth>} />
            <Route path="/kra-kpi-report" element={<KraKpiReport/>}/>
            <Route path="/kra-kpi-emp-list" element={<KraKpiList/>}/>
            <Route path="/kra-kpi-upload" element={<KraKpiUpload/>}/>
            <Route  path="/past-employees" element={<PastEmployee/>}/>
            

            {/* Manager Routes */}
            <Route path="/manager-dashboard" element={<RequireAuth allowedRoles={['MANAGER']}><ManagerDashboard /></RequireAuth>} />
            <Route path="/my-team/:id" element={<RequireAuth allowedRoles={['MANAGER']}><Team /></RequireAuth>} />
            <Route path="/manager-profile" element={<RequireAuth allowedRoles={['MANAGER']}><ManagerProfile /></RequireAuth>} />
            <Route path="/manager-review/:id/:manager" element={<RequireAuth allowedRoles={['MANAGER']}><PerformanceReview /></RequireAuth>} />
            <Route path="/completed-assessments/:id" element={<RequireAuth allowedRoles={['MANAGER']}><Complete /></RequireAuth>} />
            <Route path="/pending-assessments/:id" element={<RequireAuth allowedRoles={['MANAGER']}><Pending /></RequireAuth>} />
            <Route path="/approve-pms/:id" element={<RequireAuth allowedRoles={['MANAGER']}><Approve /></RequireAuth>} />

            {/* Employee Routes */}
            <Route path="/employee-dashboard" element={<RequireAuth allowedRoles={['EMPLOYEE']}><EmployeeDashboard /></RequireAuth>} />
            <Route path="/employee-performance" element={<Performance />}/>
            <Route path="/add-krakpi/:id" element={<RequireAuth allowedRoles={['EMPLOYEE']}><KrakpiRegistration /></RequireAuth>} />
            <Route path="/self-review/:id" element={<EmployeeReview />} />

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
    </div>
  );
}

export default App;