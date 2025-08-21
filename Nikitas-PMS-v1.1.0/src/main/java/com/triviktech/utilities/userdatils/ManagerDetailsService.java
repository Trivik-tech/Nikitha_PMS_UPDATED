package com.triviktech.utilities.userdatils;

import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.repositories.manager.ManagerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class implementing Spring Security's UserDetailsService.
 * <p>
 * Responsible for loading Manager-specific user details for authentication.
 * Fetches Manager entity data from the database and returns a UserDetails object
 * to be used by Spring Security during login.
 */
@Service
public class ManagerDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    /**
     * Constructor-based dependency injection of ManagerRepository.
     *
     * @param managerRepository Repository to fetch Manager data
     */
    public ManagerDetailsService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * Loads Manager user details by username (Manager ID) for Spring Security authentication.
     *
     * @param username Manager ID provided during login
     * @return UserDetails object containing username, password, and roles
     * @throws UsernameNotFoundException if Manager with the given ID does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch Manager entity by ID. Throw custom exception if not found.
        Manager manager = managerRepository.findById(username)
                .orElseThrow(() -> new ManagerNotFoundException(username));

        // Build a Spring Security UserDetails object with username, password, and roles
        return User.builder()
                .username(manager.getManagerId()) // Set username as Manager ID
                .password(manager.getPassword())  // Set password (Spring Security handles encoding)
                .roles(manager.getRole())         // Assign role(s) for authentication/authorization
                .build();
    }
}
