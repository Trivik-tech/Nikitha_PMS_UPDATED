package com.triviktech.repositories.address;

import com.triviktech.entities.address.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
}