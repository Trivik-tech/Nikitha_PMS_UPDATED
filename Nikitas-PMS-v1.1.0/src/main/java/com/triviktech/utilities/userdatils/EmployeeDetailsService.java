package com.triviktech.utilities.userdatils;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeInformationRepository employeeInformationRepository;

    public EmployeeDetailsService(EmployeeInformationRepository employeeInformationRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeInformation employeeInformation = employeeInformationRepository.findById(username).orElseThrow(() -> new EmployeeNotFoundException(username));

        return User.builder()
                .username(employeeInformation.getEmpId())
                .password(employeeInformation.getPassword()).roles(employeeInformation.getRole()).build();

    }
}
