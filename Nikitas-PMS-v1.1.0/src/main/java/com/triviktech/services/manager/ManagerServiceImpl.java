package com.triviktech.services.manager;


import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.address.StateNotFoundException;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.manager.ManagerAlreadyExistsException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService{

    private final ManagerRepository managerRepository;
    private final StateRepository stateRepository;
    private final LocationRepository locationRepository;
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;

    private final EntityDtoConversion entityDtoConversion;

    private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    public ManagerServiceImpl(ManagerRepository managerRepository, StateRepository stateRepository, LocationRepository locationRepository, ProjectRepository projectRepository, DepartmentRepository departmentRepository, EmployeeInformationRepository employeeInformationRepository, KraKpiRepository kraKpiRepository, EntityDtoConversion entityDtoConversion) {
        this.managerRepository = managerRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.entityDtoConversion = entityDtoConversion;
    }

    @Override
    public ManagerResponseDto registerManager(ManagerRequestDto managerRequestDto) {

        try {
            if (managerRepository.existsById(managerRequestDto.getManagerId())) {
                throw new ManagerAlreadyExistsException(managerRequestDto.getManagerId());
            }
            Manager manager = entityDtoConversion.dtoToEntityConversion(managerRequestDto,Manager.class);
            manager.setPassword(BCrypt.hashpw(managerRequestDto.getPassword(),BCrypt.gensalt(10)));

            Location location = new Location();
            location.setName(managerRequestDto.getLocationName());
            location.setZipCode(managerRequestDto.getZipCode());

            State state = stateRepository.findById(managerRequestDto.getStateId())
                    .orElseThrow(() -> new StateNotFoundException(managerRequestDto.getStateId()));
            location.setState(state);

            Location savedLocation = locationRepository.save(location);
            manager.setLocation(savedLocation);

            Department department = departmentRepository.findById(managerRequestDto.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(managerRequestDto.getDepartmentId()));
            manager.setDepartment(department);

            Set<Project> projects = new HashSet<>();
            Project project = projectRepository.findById(managerRequestDto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(managerRequestDto.getProjectId()));
            projects.add(project);
            manager.setProjects(projects);

            Manager savedManager = managerRepository.save(manager);


            ManagerResponseDto responseDto = entityDtoConversion.entityToDtoConversion(savedManager, ManagerResponseDto.class);

            LocationResponseDto locationResponseDto = entityDtoConversion.entityToDtoConversion(savedManager.getLocation(), LocationResponseDto.class);

            StateResponseDto stateResponse = entityDtoConversion.entityToDtoConversion(savedManager.getLocation().getState(),StateResponseDto.class);
            CountryResponseDto countryResponseDto = entityDtoConversion.entityToDtoConversion(savedManager.getLocation().getState().getCountry(), CountryResponseDto.class);

            stateResponse.setCountry(countryResponseDto);
            locationResponseDto.setState(stateResponse);
            responseDto.setLocation(locationResponseDto);

            DepartmentResponseDto departmentResponseDto = entityDtoConversion.entityToDtoConversion(savedManager.getDepartment(), DepartmentResponseDto.class);
            responseDto.setDepartment(departmentResponseDto);

            Set<ProjectResponseDto> projectsDto = savedManager.getProjects()
                    .stream()
                    .map(project1 -> entityDtoConversion.entityToDtoConversion(project1,ProjectResponseDto.class))
                    .collect(Collectors.toSet());
            responseDto.setProjects(projectsDto);

            return responseDto;
        }catch (Exception e){
            logger.error("Error registering manager: {}", e.getMessage());
            throw e;
        }


    }

    @Override
    public List<ManagerResponseDto> listOfManager() {

        try {
            List<Manager> all = managerRepository.findAll();
            return all.stream().map(manager -> {
                ManagerResponseDto responseDto = entityDtoConversion.entityToDtoConversion(manager, ManagerResponseDto.class);
                LocationResponseDto locationResponseDto = entityDtoConversion.entityToDtoConversion(manager.getLocation(), LocationResponseDto.class);
                StateResponseDto stateResponse = entityDtoConversion.entityToDtoConversion(manager.getLocation().getState(), StateResponseDto.class);
                CountryResponseDto countryResponseDto = entityDtoConversion.entityToDtoConversion(manager.getLocation().getState().getCountry(), CountryResponseDto.class);

                stateResponse.setCountry(countryResponseDto);
                locationResponseDto.setState(stateResponse);
                responseDto.setLocation(locationResponseDto);

                DepartmentResponseDto departmentResponseDto = entityDtoConversion.entityToDtoConversion(manager.getDepartment(), DepartmentResponseDto.class);
                responseDto.setDepartment(departmentResponseDto);

                Set<ProjectResponseDto> projectsDto = manager.getProjects()
                        .stream()
                        .map(project -> entityDtoConversion.entityToDtoConversion(project,ProjectResponseDto.class))
                        .collect(Collectors.toSet());
                responseDto.setProjects(projectsDto);

                return responseDto;
            }).toList();


        }catch (Exception e){
            logger.error("Error retrieving managers: {}", e.getMessage());
            throw e;
        }



    }

    @Override
    public ManagerResponseDto findManagerById(String managerId) {

        try {
            Manager manager = managerRepository
                    .findById(managerId).orElseThrow(() -> new ManagerNotFoundException(managerId));

            ManagerResponseDto responseDto = entityDtoConversion.entityToDtoConversion(manager,ManagerResponseDto.class);
            LocationResponseDto locationResponseDto = entityDtoConversion.entityToDtoConversion(manager.getLocation(), LocationResponseDto.class);
            StateResponseDto stateResponse = entityDtoConversion.entityToDtoConversion(manager.getLocation().getState(), StateResponseDto.class);
            CountryResponseDto countryResponseDto = entityDtoConversion.entityToDtoConversion(manager.getLocation().getState().getCountry(), CountryResponseDto.class);

            stateResponse.setCountry(countryResponseDto);
            locationResponseDto.setState(stateResponse);
            responseDto.setLocation(locationResponseDto);

            DepartmentResponseDto departmentResponseDto = entityDtoConversion.entityToDtoConversion(manager.getDepartment(), DepartmentResponseDto.class);
            responseDto.setDepartment(departmentResponseDto);

            Set<ProjectResponseDto> projectsDto = manager.getProjects()
                    .stream()
                    .map(project -> entityDtoConversion.entityToDtoConversion(project,ProjectResponseDto.class))
                    .collect(Collectors.toSet());
            responseDto.setProjects(projectsDto);

            return responseDto;
        }catch (Exception e){
            logger.error("Error finding manager with ID {}: {}", managerId, e.getMessage());
            throw e;
        }

    }

    @Override
    public List<EmployeeWithPmsStatus> listOfEmployeesForManager(String managerId) {

//        try {
//            Manager manager = managerRepository.findById(managerId)
//                    .orElseThrow(() -> new ManagerNotFoundException(managerId));
//
//            List<EmployeeInformation> allByManager = employeeInformationRepository.findAllByManager(manager);
//
//            return allByManager.stream()
//                    .map(employee -> {
//                        Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
//                        KraKpi kraKpi;
//                        if(kraKpiOptional.isPresent()){
//                            kraKpi = kraKpiOptional.get();
//                            kraKpi.setPmsInitiated(true);
//
//                        }else{
//                            kraKpi = new KraKpi();
//                            kraKpi.setPmsInitiated(false);
//                            kraKpi.setManagerCompleted(false);
//                            kraKpi.setSelfCompleted(false);
//                        }
//                        return new EmployeeWithKraKpi(employee, kraKpi);
//
//
//                    })
//                    .filter(pair -> pair.kraKpi.getPmsInitiated())  // Filter only if PMS is initiated
//                    .map(pair -> {
//                        EmployeeInformation employee = pair.employee;
//                        KraKpi kraKpi = pair.kraKpi;
//
//                        EmployeeWithPmsStatus response = entityDtoConversion.entityToDtoConversion(employee, EmployeeWithPmsStatus.class);
//                        response.setManagerCompleted(kraKpi.isManagerCompleted());
//                        response.setSelfCompleted(kraKpi.isSelfCompleted());
//                        response.setPmsInitiated(kraKpi.getPmsInitiated());
//                        response.setDepartment(entityDtoConversion.entityToDtoConversion(employee.getDepartment(), DepartmentResponseDto.class));
//
//                        return response;
//                    })
//                    .toList();
//
//        } catch (Exception e) {
//            logger.error("Error retrieving employees for manager ID {}: {}", managerId, e.getMessage());
//            throw e;
//        }
        return null;
    }

    @Override
    public List<EmployeeWithPmsStatus> listOfPMSCompletedEmployees(String managerId) {
//        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ManagerNotFoundException(managerId));
//        List<EmployeeInformation> allEmployees = employeeInformationRepository.findAllByManager(manager);
//        return allEmployees.stream()
//                .map(employee -> {
//                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
//                    KraKpi kraKpi;
//                    if(kraKpiOptional.isPresent()){
//                        kraKpi = kraKpiOptional.get();
//                        kraKpi.setPmsInitiated(true);
//
//                    }else{
//                        kraKpi = new KraKpi();
//                        kraKpi.setPmsInitiated(false);
//                        kraKpi.setManagerCompleted(false);
//                        kraKpi.setSelfCompleted(false);
//                    }
//                    return new EmployeeWithKraKpi(employee, kraKpi);
//
//
//                })
//                .filter(pair -> pair.kraKpi.getPmsInitiated() && pair.kraKpi.isManagerCompleted() && pair.kraKpi.isSelfCompleted())  // Filter only if PMS is initiated
//                .map(pair -> {
//                    EmployeeInformation employee = pair.employee;
//                    KraKpi kraKpi = pair.kraKpi;
//
//                    EmployeeWithPmsStatus response = entityDtoConversion.entityToDtoConversion(employee, EmployeeWithPmsStatus.class);
//                    response.setManagerCompleted(kraKpi.isManagerCompleted());
//                    response.setSelfCompleted(kraKpi.isSelfCompleted());
//                    response.setPmsInitiated(kraKpi.getPmsInitiated());
//                    response.setDepartment(entityDtoConversion.entityToDtoConversion(employee.getDepartment(), DepartmentResponseDto.class));
//
//                    return response;
//                })
//                .toList();

        return null;
    }

    @Override
    public List<EmployeeWithPmsStatus> listOfPMSPendingEmployees(String managerId) {
//        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new ManagerNotFoundException(managerId));
//        List<EmployeeInformation> allEmployees = employeeInformationRepository.findAllByManager(manager);
//        return allEmployees.stream()
//                .map(employee -> {
//                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
//                    KraKpi kraKpi;
//                    if(kraKpiOptional.isPresent()){
//                        kraKpi = kraKpiOptional.get();
//                        kraKpi.setPmsInitiated(true);
//
//                    }else{
//                        kraKpi = new KraKpi();
//                        kraKpi.setPmsInitiated(false);
//                        kraKpi.setManagerCompleted(false);
//                        kraKpi.setSelfCompleted(false);
//                    }
//                    return new EmployeeWithKraKpi(employee, kraKpi);
//
//
//                })
//                .filter(pair -> pair.kraKpi.getPmsInitiated() && (!pair.kraKpi.isManagerCompleted() || !pair.kraKpi.isSelfCompleted()))  // Filter only if PMS is initiated
//                .map(pair -> {
//                    EmployeeInformation employee = pair.employee;
//                    KraKpi kraKpi = pair.kraKpi;
//
//                    EmployeeWithPmsStatus response = entityDtoConversion.entityToDtoConversion(employee, EmployeeWithPmsStatus.class);
//                    response.setManagerCompleted(kraKpi.isManagerCompleted());
//                    response.setSelfCompleted(kraKpi.isSelfCompleted());
//                    response.setPmsInitiated(kraKpi.getPmsInitiated());
//                    response.setDepartment(entityDtoConversion.entityToDtoConversion(employee.getDepartment(), DepartmentResponseDto.class));
//
//                    return response;
//                })
//                .toList();
        return null;
    }

    // Helper record to carry both Employee and KraKpi together in the stream
    private record EmployeeWithKraKpi(EmployeeInformation employee, KraKpi kraKpi) {}





}
