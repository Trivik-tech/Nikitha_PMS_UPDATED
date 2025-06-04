package com.triviktech.services.manager;


import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.kpi.KPI;
import com.triviktech.entities.kra.KRA;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.address.StateNotFoundException;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.krakpi.KraKpiNotFoundException;
import com.triviktech.exception.manager.ManagerAlreadyExistsException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.kpi.KpiRequestDto;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.kpi.KpiResponseDto;
import com.triviktech.payloads.response.kra.KraResponseDto1;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
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
import jakarta.websocket.DeploymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<EmployeeWithPmsStatus> listOfEmployeesForManager(String reportingManager) {
        try {
            List<EmployeeInformation> allByManager = employeeInformationRepository.findAllByReportingManager(reportingManager);

            return allByManager.stream().map(employee -> {
                // Convert base employee info to response DTO
                EmployeeWithPmsStatus employeeResponse =
                        entityDtoConversion.entityToDtoConversion(employee, EmployeeWithPmsStatus.class);

                // Set department info
                employeeResponse.setDepartment(
                        entityDtoConversion.entityToDtoConversion(employee.getDepartment(), DepartmentResponseDto.class)
                );

                // Fetch KraKpi if present
                Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);

                // Set PMS status fields only if KraKpi exists
                kraKpiOptional.ifPresent(kraKpi -> {
                    employeeResponse.setPmsInitiated(kraKpi.getPmsInitiated());
                    employeeResponse.setSelfCompleted(kraKpi.isSelfCompleted());
                });

                return employeeResponse;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error retrieving employees for manager ID {}: {}", reportingManager, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public List<EmployeeInfo> findAllByReportingManager(String reportingManager) {
        List<EmployeeInformation> allEmployees = employeeInformationRepository.findAllByReportingManager(reportingManager);
        List<EmployeeInfo> collect = allEmployees.stream().map(employee -> {
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);
            employeeInfo.setDepartment(employee.getDepartment().getName());
            return employeeInfo;

        }).collect(Collectors.toList());
        return collect;
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

    @Override
    public KraKpiResponseDto getEmployeeKarKpi(String managerName, String employeeId) {
        Optional<EmployeeInformation> employeeData = employeeInformationRepository.findByReportingManagerAndEmpId(managerName, employeeId);
           if(employeeData.isPresent()){
               EmployeeInformation employee = employeeData.get();
               Optional<KraKpi> kraKpi = kraKpiRepository.findByEmployeeInformation(employee);
                if (kraKpi.isPresent()){
                    KraKpi kraKpi1 = kraKpi.get();
                    KraKpiResponseDto kraKpiResponseDto = entityDtoConversion.entityToDtoConversion(kraKpi1, KraKpiResponseDto.class);
                    EmployeeInformation employeeInformation = kraKpi1.getEmployeeInformation();
                    EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employeeInformation, EmployeeInfo.class);
                    employeeInfo.setDepartment(employeeInformation.getDepartment().getName());
                    kraKpiResponseDto.setEmployee(employeeInfo);

                    Set<KraResponseDto1> kras = kraKpi1.getKra().stream().map(kra -> {
                        KraResponseDto1 kraResponseDto = entityDtoConversion.entityToDtoConversion(kra, KraResponseDto1.class);

                        Set<KpiResponseDto> kpis = kra.getKpi().stream().map(kpi -> {
                            KpiResponseDto kpiResponseDto = entityDtoConversion.entityToDtoConversion(kpi, KpiResponseDto.class);

                            return kpiResponseDto;

                        }).collect(Collectors.toSet());

                        kraResponseDto.setKpi(kpis);
                        return kraResponseDto;

                    }).collect(Collectors.toSet());

                    kraKpiResponseDto.setKra(kras);

                    return kraKpiResponseDto;

                }
                else{
                    throw new KraKpiNotFoundException("Kra Kpi not found for employee with ID " + employeeId);
                }

           }
           else{

               throw new EmployeeNotFoundException(employeeId);
               
           }
    }

    @Override
    public Map<String, String> approveKra(KraKpiRequestDto kraKpiRequestDto, String employeeId, String reportingManager) {
        Map<String, String> response = new HashMap<>();
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);

        if (employeeById.isEmpty()) {
           throw new EmployeeNotFoundException(employeeId);
        }

        EmployeeInformation employee = employeeById.get();
        Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);

        if (kraKpiOptional.isEmpty()) {
            throw new KraKpiNotFoundException("Kra Kpi Not found for employee with id :"+employeeId);
        }

        KraKpi kraKpi = kraKpiOptional.get();
        kraKpi.setSelfCompleted(kraKpiRequestDto.getSelfCompleted());
        kraKpi.setManagerApproval(kraKpiRequestDto.getManagerApproval());
        Set<KRA> existingKras = kraKpi.getKra();
        Set<KRA> updatedKras = new HashSet<>();

        for (KraRequestDto kraDto : kraKpiRequestDto.getKra()) {
            // Find existing or create new KRA
            KRA kra = existingKras.stream()
                    .filter(existingKra -> existingKra.getKraId().equals(kraDto.getKraId()))
                    .findFirst()
                    .orElse(new KRA());

            kra.setKraKpi(kraKpi);
            kra.setKraName(kraDto.getKraName());
            kra.setWeightage(kraDto.getWeightage());

            // Update KPIs
            Set<KPI> existingKpis = kra.getKpi() != null ? kra.getKpi() : new HashSet<>();
            Set<KPI> updatedKpis = new HashSet<>();

            for (KpiRequestDto kpiDto : kraDto.getKpi()) {
                KPI kpi = existingKpis.stream()
                        .filter(existingKpi -> existingKpi.getKpiId().equals(kpiDto.getKpiId()))
                        .findFirst()
                        .orElse(new KPI());

                kpi.setKra(kra);
                kpi.setDescription(kpiDto.getDescription());
                kpi.setWeightage(kpiDto.getWeightage());
                kpi.setSelfScore(kpiDto.getSelfScore());
                kpi.setManagerScore(kpiDto.getManagerScore());
                kpi.setAverage((float) (kpiDto.getSelfScore() + kpiDto.getManagerScore()) / 2);
                kpi.setReview2(kpiDto.getReview2());

                updatedKpis.add(kpi);
            }

            // Remove KPIs that are not in the update
            existingKpis.removeIf(existingKpi ->
                    updatedKpis.stream().noneMatch(updatedKpi ->
                            updatedKpi.getKpiId().equals(existingKpi.getKpiId()))
            );

            existingKpis.addAll(updatedKpis);
            kra.setKpi(existingKpis);
            updatedKras.add(kra);
        }

        // Remove KRAs that are not in the update
        existingKras.removeIf(existingKra ->
                updatedKras.stream().noneMatch(updatedKra ->
                        updatedKra.getKraId().equals(existingKra.getKraId()))
        );

        existingKras.addAll(updatedKras);
        kraKpi.setKra(existingKras);

        KraKpi kraKpi1 = kraKpiRepository.saveAndFlush(kraKpi);
        String msg=kraKpi1!=null?"Approved":"Something went wrong";
        response.put("status",msg);

        return response ;
    }

    @Override
    public Map<String,String> managerReview(String managerName,String employeeId, KraKpiRequestDto data) {
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findByReportingManagerAndEmpId(managerName, employeeId);
        if(employeeById.isPresent()){
            EmployeeInformation employee = employeeById.get();
            Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
            if(kraKpiOptional.isPresent()){
                KraKpi kraKpi = kraKpiOptional.get();
                kraKpi.setRemark(data.getRemark());
                kraKpi.setSelfCompleted(data.getSelfCompleted());
                kraKpi.setManagerCompleted(data.isManagerCompleted());
                kraKpi.setDueDate(data.getDueDate());
                kraKpi.setManagerReviewDate(data.getManagerReviewDate());
                kraKpi.setSelfReviewDate(data.getSelfReviewDate());
                kraKpi.setPmsInitiated(data.getPmsInitiated());
                kraKpi.setReview2(data.isReview2());
                kraKpi.setManagerApproval(data.getManagerApproval());

                // Track existing KRAs for potential removal
                Set<KRA> existingKras = kraKpi.getKra();
                Set<KRA> updatedKras = new HashSet<>();

                for (KraRequestDto kraDto : data.getKra()) {
                    // Try to find existing KRA, or create new if not found
                    KRA kra = existingKras.stream()
                            .filter(existingKra -> existingKra.getKraId() == kraDto.getKraId())
                            .findFirst()
                            .orElse(new KRA());  // New KRA if not found

                    kra.setKraKpi(kraKpi);
                    kra.setKraName(kraDto.getKraName());
                    kra.setWeightage(kraDto.getWeightage());

                    // Update KPIs within this KRA
                    Set<KPI> existingKpis = kra.getKpi() != null ? kra.getKpi() : new HashSet<>();
                    Set<KPI> updatedKpis = new HashSet<>();

                    for (KpiRequestDto kpiDto : kraDto.getKpi()) {
                        // Try to find existing KPI or create new
                        KPI kpi = existingKpis.stream()
                                .filter(existingKpi -> existingKpi.getKpiId() == kpiDto.getKpiId())
                                .findFirst()
                                .orElse(new KPI());  // New KPI if not found

                        kpi.setKra(kra);  // Set parent
                        kpi.setDescription(kpiDto.getDescription());
                        kpi.setWeightage(kpiDto.getWeightage());
                        kpi.setSelfScore(kpiDto.getSelfScore());
                        kpi.setManagerScore(kpiDto.getManagerScore());
                        kpi.setAverage((float) (kpiDto.getSelfScore() + kpiDto.getManagerScore()) / 2);
                        kpi.setReview2(kpiDto.getReview2());

                        updatedKpis.add(kpi);
                    }

                    // Remove KPIs that were not in the new request (to handle deletions)
                    existingKpis.removeIf(existingKpi ->
                            updatedKpis.stream().noneMatch(updatedKpi -> updatedKpi.getKpiId() == existingKpi.getKpiId())
                    );

                    // Combine updated and remaining existing KPIs
                    existingKpis.addAll(updatedKpis);
                    kra.setKpi(existingKpis);

                    updatedKras.add(kra);
                }

                // Remove KRAs that are no longer in the request (handle deletion if needed)
                existingKras.removeIf(existingKra ->
                        updatedKras.stream().noneMatch(updatedKra -> updatedKra.getKraId() == existingKra.getKraId())
                );

                // Combine updated and remaining existing KRAs
                existingKras.addAll(updatedKras);
                kraKpi.setKra(existingKras);

                // Save the entire structure (KraKpi -> KRA -> KPI)
                kraKpiRepository.saveAndFlush(kraKpi);

            }else{
                throw new KraKpiNotFoundException("Kra Kpi not found for employee with id "+employeeId);
            }

        }else{
            throw new EmployeeNotFoundException(employeeId);
        }

        return Map.of("status","Manager Review has been completed");
    }


    // Helper record to carry both Employee and KraKpi together in the stream
    private record EmployeeWithKraKpi(EmployeeInformation employee, KraKpi kraKpi) {}





}
