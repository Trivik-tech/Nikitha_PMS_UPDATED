package com.triviktech.repositories.address;

import com.triviktech.entities.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}