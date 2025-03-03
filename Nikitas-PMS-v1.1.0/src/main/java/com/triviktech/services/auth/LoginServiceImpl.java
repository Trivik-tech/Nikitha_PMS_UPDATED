package com.triviktech.services.auth;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import com.triviktech.payloads.login.Login;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.utilities.jwt.JwtService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService{

    private final JwtService jwtService;

    private final ManagerRepository managerRepository;
    private final HRRepository hrRepository;
    private final EmployeeInformationRepository employeeInformationRepository;

    public LoginServiceImpl(JwtService jwtService, ManagerRepository managerRepository, HRRepository hrRepository, EmployeeInformationRepository employeeInformationRepository) {
        this.jwtService = jwtService;

        this.managerRepository = managerRepository;
        this.hrRepository = hrRepository;
        this.employeeInformationRepository = employeeInformationRepository;
    }

    @Override
    public String verifyLogin(Login login) {
        if(managerRepository.existsById(login.getUsername())){
            Optional<Manager> manager = managerRepository.findById(login.getUsername());
            if(manager.isPresent()){
                Manager manager1 = manager.get();
                if(BCrypt.checkpw(login.getPassword(),manager1.getPassword())){
                    return jwtService.generateToken(manager1.getManagerId(),manager1.getRole());
                }
            }
        }
        else if(hrRepository.existsById(login.getUsername())){
            Optional<HR> hr = hrRepository.findById(login.getUsername());
            if(hr.isPresent()){
                HR hr1 = hr.get();
                if(BCrypt.checkpw(login.getPassword(),hr1.getPassword())){
                    return jwtService.generateToken(hr1.getHrId(),hr1.getRole());
                }
            }

        }
        else if (employeeInformationRepository.existsById(login.getUsername())){
            Optional<EmployeeInformation> employee = employeeInformationRepository.findById(login.getUsername());
            if(employee.isPresent()){
                EmployeeInformation employeeInformation = employee.get();
                if(BCrypt.checkpw(login.getPassword(),employeeInformation.getPassword())){
                    return jwtService.generateToken(employeeInformation.getEmployeeId(),employeeInformation.getRole());
                }
            }

        }
        return null;
    }
}
