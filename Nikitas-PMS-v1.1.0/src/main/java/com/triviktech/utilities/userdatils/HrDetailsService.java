package com.triviktech.utilities.userdatils;

import com.triviktech.entities.hr.HR;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.repositories.hr.HRRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class that implements Spring Security's UserDetailsService.
 * <p>
 * Responsible for loading HR-specific user details for authentication.
 * It fetches HR entity data from the database and returns a UserDetails object
 * to be used by Spring Security during login.
 */
@Service
public class HrDetailsService implements UserDetailsService {

    private final HRRepository hrRepository;

    /**
     * Constructor-based dependency injection of HRRepository.
     *
     * @param hrRepository Repository to fetch HR data
     */
    public HrDetailsService(HRRepository hrRepository) {
        this.hrRepository = hrRepository;
    }

    /**
     * Loads HR user details by username (HR ID) for Spring Security authentication.
     *
     * @param username HR ID provided during login
     * @return UserDetails object containing username, password, and roles
     * @throws UsernameNotFoundException if HR with the given ID does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch HR entity by ID. Throw custom exception if not found.
        HR hr = hrRepository.findById(username)
                .orElseThrow(() -> new HRNotFoundException(username));

        // Build a Spring Security UserDetails object with username, password, and roles
        return User.builder()
                .username(hr.getHrId())      // Set username as HR ID
                .password(hr.getPassword())  // Set password (Spring Security handles encoding)
                .roles(hr.getRole())         // Assign role(s) for authentication/authorization
                .build();
    }
}
