package com.triviktech.services.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeInformationRepository employeeInformationRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final KraKpiRepository kraKpiRepository;

    public EmployeeServiceImpl(EmployeeInformationRepository employeeInformationRepository, EntityDtoConversion entityDtoConversion, DepartmentRepository departmentRepository, ProjectRepository projectRepository, ManagerRepository managerRepository, KraKpiRepository kraKpiRepository) {
        this.employeeInformationRepository = employeeInformationRepository;

        this.entityDtoConversion = entityDtoConversion;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.managerRepository = managerRepository;
        this.kraKpiRepository = kraKpiRepository;
    }


    @Override
    public List<EmployeeInformationResponseDto> listOfEmployees(int pageSize,int pageNumber) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeInformation> all = employeeInformationRepository.findAll(pageRequest);

        List<EmployeeInformation> content = all.getContent();
       return content.stream().map(employee->{

            EmployeeInformationResponseDto employeeInformationResponseDto = entityDtoConversion.entityToDtoConversion(employee, EmployeeInformationResponseDto.class);

            employeeInformationResponseDto.setProjects(employee.getProjects().stream()
                    .map(project -> entityDtoConversion.entityToDtoConversion(project,ProjectResponseDto.class)).collect(Collectors.toSet()));

            employeeInformationResponseDto.setDepartment(entityDtoConversion.entityToDtoConversion(employee.getDepartment(),DepartmentResponseDto.class));
//            employeeInformationResponseDto.setManager(entityDtoConversion.entityToDtoConversion(employee.getManager(), ManagerResponseDto.class));
            return employeeInformationResponseDto;

        }).toList();

    }

    @Override
    public EmployeeInfo profile(String employeeId) {
        Optional<EmployeeInformation> byId = employeeInformationRepository.findById(employeeId);
          if(byId.isPresent()) {
            EmployeeInformation employeeInformation = byId.get();
              EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employeeInformation, EmployeeInfo.class);
              employeeInfo.setDepartment(employeeInformation.getDepartment().getName());
              return  employeeInfo;
        }else{
            throw new EmployeeNotFoundException(employeeId);
        }

    }




}
