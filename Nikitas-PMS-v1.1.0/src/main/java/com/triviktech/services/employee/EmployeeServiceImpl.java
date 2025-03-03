package com.triviktech.services.employee;

import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeInformationRepository employeeInformationRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;

    public EmployeeServiceImpl(EmployeeInformationRepository employeeInformationRepository, EntityDtoConversion entityDtoConversion, DepartmentRepository departmentRepository, ProjectRepository projectRepository, ManagerRepository managerRepository) {
        this.employeeInformationRepository = employeeInformationRepository;

        this.entityDtoConversion = entityDtoConversion;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public EmployeeInformationResponseDto registerEmployee(EmployeeInformationRequestDto employeeInformationRequestDto) {

        EmployeeInformation employeeInformation = entityDtoConversion.dtoToEntityConversion(employeeInformationRequestDto, EmployeeInformation.class);
        employeeInformation.setEmployeeId(employeeInformationRequestDto.getEmployeeId());
        employeeInformation.setPassword(BCrypt.hashpw(employeeInformation.getPassword(),BCrypt.gensalt(10)));
        employeeInformation.setDepartment(departmentRepository
                        .findById(employeeInformationRequestDto.getDepartmentId()).orElseThrow(()-> new DepartmentNotFoundException(employeeInformationRequestDto.getDepartmentId()))
                );
        employeeInformation.setManager(managerRepository
                .findById(employeeInformationRequestDto.getManagerId()).orElseThrow(()-> new ManagerNotFoundException(employeeInformationRequestDto.getManagerId())));

        Set<Project> projects=new HashSet<>();
        Project project1 = projectRepository.findById(employeeInformationRequestDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(employeeInformationRequestDto.getProjectId()));
        projects.add(project1);
        employeeInformation.setProjects(projects);


        EmployeeInformation saved = employeeInformationRepository.save(employeeInformation);

        EmployeeInformationResponseDto employeeInformationResponseDto = entityDtoConversion.entityToDtoConversion(saved, EmployeeInformationResponseDto.class);

        employeeInformationResponseDto.setManager(entityDtoConversion.entityToDtoConversion(saved.getManager(), ManagerResponseDto.class));
        employeeInformationResponseDto.setDepartment(entityDtoConversion.entityToDtoConversion(saved.getDepartment(),DepartmentResponseDto.class));

        employeeInformationResponseDto.setProjects(saved.getProjects().stream().map(project -> entityDtoConversion.entityToDtoConversion(project, ProjectResponseDto.class)).collect(Collectors.toSet()));

        return employeeInformationResponseDto;
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
            employeeInformationResponseDto.setManager(entityDtoConversion.entityToDtoConversion(employee.getManager(), ManagerResponseDto.class));
            return employeeInformationResponseDto;

        }).toList();

    }


}
