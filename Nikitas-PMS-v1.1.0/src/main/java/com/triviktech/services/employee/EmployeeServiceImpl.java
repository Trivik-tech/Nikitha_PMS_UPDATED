package com.triviktech.services.employee;

import com.triviktech.entities.employee.EmployeeInformation;

import com.triviktech.exception.employee.EmployeeNotFoundException;

import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.repositories.employee.EmployeeInformationRepository;

import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the {@link EmployeeService} interface.
 * <p>
 * This service provides business logic for handling employee-related operations,
 * such as fetching employee profile details from the database and converting them
 * into DTOs for response.
 * </p>
 */

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeInformationRepository employeeInformationRepository;
    private final EntityDtoConversion entityDtoConversion;


    public EmployeeServiceImpl(EmployeeInformationRepository employeeInformationRepository, EntityDtoConversion entityDtoConversion) {
        this.employeeInformationRepository = employeeInformationRepository;

        this.entityDtoConversion = entityDtoConversion;

    }


    @Override
    public EmployeeInfo profile(String employeeId) {
        Optional<EmployeeInformation> byId = employeeInformationRepository.findById(employeeId);
          if(byId.isPresent()) {
            EmployeeInformation employeeInformation = byId.get();
              EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employeeInformation, EmployeeInfo.class);
              employeeInfo.setDepartment(employeeInformation.getDepartment().getName());
              employeeInfo.setReportingManager(employeeInformation.getManager().getName());
              return  employeeInfo;
        }else{
            throw new EmployeeNotFoundException(employeeId);
        }

    }




}
