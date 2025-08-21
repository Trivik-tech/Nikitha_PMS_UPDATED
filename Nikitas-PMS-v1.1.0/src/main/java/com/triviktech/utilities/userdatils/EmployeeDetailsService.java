package com.triviktech.utilities.userdatils;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class that implements Spring Security's UserDetailsService.
 * <p>
 * Responsible for loading user-specific data during authentication.
 * In this case, it loads EmployeeInformation from the database and
 * returns a Spring Security UserDetails object for authentication.
 */
@Service
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeInformationRepository employeeInformationRepository;

    /**
     * Constructor-based dependency injection of EmployeeInformationRepository
     *
     * @param employeeInformationRepository Repository to fetch employee data
     */
    public EmployeeDetailsService(EmployeeInformationRepository employeeInformationRepository) {
        this.employeeInformationRepository = employeeInformationRepository;
    }

    /**
     * Loads a user by username (employee ID) for Spring Security authentication.
     *
     * @param username Employee ID provided during login
     * @return UserDetails object containing username, password, and roles
     * @throws UsernameNotFoundException if employee with given ID does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch employee information by ID. Throw custom exception if not found.
        EmployeeInformation employeeInformation = employeeInformationRepository.findById(username)
                .orElseThrow(() -> new EmployeeNotFoundException(username));

        // Build a Spring Security UserDetails object with username, password, and roles.
        return User.builder()
                .username(employeeInformation.getEmpId())     // Set username as employee ID
                .password(employeeInformation.getPassword())  // Set password (Spring Security handles encoding)
                .roles(employeeInformation.getRole())         // Assign role(s) for authentication/authorization
                .build();
    }
}
