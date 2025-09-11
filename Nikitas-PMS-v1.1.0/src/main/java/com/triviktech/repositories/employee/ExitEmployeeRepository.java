package com.triviktech.repositories.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triviktech.entities.employee.ExitEmployee;

public interface ExitEmployeeRepository extends JpaRepository<ExitEmployee,String>  {

}
