package com.triviktech.utilities.userdatils;

import com.triviktech.entities.hr.HR;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.repositories.hr.HRRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HrDetailsService implements UserDetailsService {

    private final HRRepository hrRepository;

    public HrDetailsService(HRRepository hrRepository) {
        this.hrRepository = hrRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HR hr = hrRepository.findById(username).orElseThrow(() -> new HRNotFoundException(username));
       return User.builder().username(hr.getHrId()).password(hr.getPassword()).roles(hr.getRole()).build();
    }
}
