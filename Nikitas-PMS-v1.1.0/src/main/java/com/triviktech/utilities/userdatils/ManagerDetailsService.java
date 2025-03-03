package com.triviktech.utilities.userdatils;

import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.repositories.manager.ManagerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ManagerDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    public ManagerDetailsService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Manager manager = managerRepository.findById(username).orElseThrow(() -> new ManagerNotFoundException(username));

        return User.builder().username(manager.getManagerId()).password(manager.getPassword()).roles(manager.getRole()).build();

    }
}
