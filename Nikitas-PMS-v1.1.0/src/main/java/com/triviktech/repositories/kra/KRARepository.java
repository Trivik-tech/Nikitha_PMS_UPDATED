package com.triviktech.repositories.kra;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kra.KRA;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface KRARepository extends JpaRepository<KRA, Long> {
    Optional<KRA> findByKraNameIgnoreCase(String kraName);
    @EntityGraph(attributePaths="kpi")
    List<KRA> findAll();


}