package com.triviktech.services.hr;

import com.triviktech.entities.address.Country;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.exception.krakpi.KraKpiNotFoundException;
import com.triviktech.payloads.request.department.DepartmentRequestDto;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employeeslist.EmployeesList;
import com.triviktech.payloads.response.global.Response;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HrServiceImpl implements HrService {

    private final HRRepository hrRepository;
    private final StateRepository stateRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final EmployeeInformationRepository employeeInformationRepository;
    private final KraKpiRepository kraKpiRepository;
    private final EmailService emailService;

    public HrServiceImpl(HRRepository hrRepository, StateRepository stateRepository,
                         LocationRepository locationRepository, DepartmentRepository departmentRepository,
                         KraKpiRepository kraKpiRepository,
                         EntityDtoConversion entityDtoConversion, EmployeeInformationRepository employeeInformationRepository, EmailService emailService) {

        this.hrRepository = hrRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.emailService = emailService;

        this.entityDtoConversion = entityDtoConversion;
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
    }

    @Override
    public HrResponseDto registerHr(HrRequestDto hrRequestDto) {
        // HR hr=new HR();
        // hr.setHrId(hrRequestDto.getHrId());
        // hr.setFirstName(hrRequestDto.getFirstName());
        // hr.setLastName(hrRequestDto.getLastName());
        // hr.setEmail(hrRequestDto.getEmail());
        // hr.setContactNumber(hrRequestDto.getContactNumber());
        // hr.setDateOfBirth(hrRequestDto.getDateOfBirth());
        // hr.setRole(hrRequestDto.getRole());
        // hr.setPassword(BCrypt.hashpw(hrRequestDto.getPassword(),BCrypt.gensalt(10)));
        //
        // Location location=new Location();
        // location.setName(hrRequestDto.getLocationName());
        // location.setZipCode(hrRequestDto.getZipCode());
        // location.setState(stateRepository.findById(hrRequestDto.getStateId())
        // .orElseThrow(()-> new StateNotFoundException(hrRequestDto.getStateId())));
        //
        // Location savedLocation = locationRepository.save(location);
        // hr.setLocation(savedLocation);
        //
        // Set<Department> departments=new HashSet<>();
        // departments.add(departmentRepository.findById(hrRequestDto.getDepartmentId())
        // .orElseThrow(()-> new
        // DepartmentNotFoundException(hrRequestDto.getDepartmentId())));
        // hr.setDepartments(departments);
        //
        // Set<Project> projects=new HashSet<>();
        // projects.add((projectRepository.findById(hrRequestDto.getProjectId())
        // .orElseThrow(()-> new
        // ProjectNotFoundException(hrRequestDto.getProjectId()))));
        // hr.setProjects(projects);
        //
        // Set<Manager> managers=new HashSet<>();
        // managers.add(managerRepository.findById(hrRequestDto.getManagerId())
        // .orElseThrow(()-> new
        // ManagerNotFoundException(hrRequestDto.getManagerId())));
        // hr.setManagers(managers);
        //
        // HR saved = hrRepository.save(hr);
        // HrResponseDto hrResponseDto = mapToHrResponseDto(saved);
        //
        // CountryResponseDto countryResponseDto =
        // mapToCountryResponseDto(saved.getLocation().getState().getCountry());
        // StateResponseDto stateResponseDto =
        // mapToStateResponseDto(saved.getLocation().getState());
        // LocationResponseDto locationResponseDto =
        // mapToLocationResponseDto(savedLocation);
        // stateResponseDto.setCountry(countryResponseDto);
        // locationResponseDto.setState(stateResponseDto);
        // hrResponseDto.setLocationResponseDto(locationResponseDto);
        //
        // Set<DepartmentResponseDto> departmentsDto =
        // saved.getDepartments().stream().map(this::mapToDepartmentResponseDto).collect(Collectors.toSet());
        //
        // hrResponseDto.setDepartments(departmentsDto);
        //
        // Set<ProjectResponseDto> projectResponseDtos =
        // saved.getProjects().stream().map(this::mapToProjectResponseDto).collect(Collectors.toSet());
        // hrResponseDto.setProjects(projectResponseDtos);
        //
        // Set<ManagerResponseDto> managerResponseDtos =
        // saved.getManagers().stream().map(this::mapToManagerResponseDto).collect(Collectors.toSet());
        //
        // hrResponseDto.setManagers(managerResponseDtos);

        // return hrResponseDto;
        return null;
    }

    @Override
    public HrResponseDto findHrById(String hrId) {
        // HR hr = hrRepository.findById(hrId).orElseThrow(() -> new
        // HRNotFoundException(hrId));
        //
        // HrResponseDto hrResponseDto = mapToHrResponseDto(hr);
        //
        // CountryResponseDto countryResponseDto =
        // mapToCountryResponseDto(hr.getLocation().getState().getCountry());
        // StateResponseDto stateResponseDto =
        // mapToStateResponseDto(hr.getLocation().getState());
        // LocationResponseDto locationResponseDto =
        // mapToLocationResponseDto(hr.getLocation());
        // stateResponseDto.setCountry(countryResponseDto);
        // locationResponseDto.setState(stateResponseDto);
        // hrResponseDto.setLocationResponseDto(locationResponseDto);

        // Set<DepartmentResponseDto> departmentsDto =
        // hr.getDepartments().stream().map(this::mapToDepartmentResponseDto).collect(Collectors.toSet());

        // System.out.println(hr.getDepartments());
        // hrResponseDto.setDepartments(departmentsDto);

        // Set<ProjectResponseDto> projectResponseDtos =
        // hr.getProjects().stream().map(this::mapToProjectResponseDto).collect(Collectors.toSet());
        // hrResponseDto.setProjects(projectResponseDtos);

        // Set<ProjectResponseDto> projectResponseDtos =
        // hr.getProjects().stream().map(this::mapToProjectResponseDto)
        // .collect(Collectors.toSet());
        // hrResponseDto.setProjects(projectResponseDtos);

        // Set<ManagerResponseDto> managerResponseDtos =
        // hr.getManagers().stream().map(this::mapToManagerResponseDto).collect(Collectors.toSet());

        // hrResponseDto.setManagers(managerResponseDtos);

        return null;
    }

    @Override
    public List<Object> uploadEmployeesData(MultipartFile file) throws IOException {
        List<Employee> employees = XlsxSupport.convertXlsxToListOfEmployee(file.getInputStream());

        // Filter out invalid or blank employee rows
        employees = employees.stream()
                .filter(emp -> emp != null &&
                        emp.getName() != null && !emp.getName().trim().isEmpty() &&
                        emp.getDepartment() != null && !emp.getDepartment().trim().isEmpty())
                .collect(Collectors.toList());

        // Cache existing departments
        Map<String, Department> departmentCache = departmentRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Department::getName, d -> d));

        // Extract departments from Excel
        Set<String> allDeptNames = employees.stream()
                .map(Employee::getDepartment)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toSet());

        // Create and save new departments if needed
        List<Department> newDepartments = allDeptNames.stream()
                .filter(dept -> !departmentCache.containsKey(dept))
                .map(name -> {
                    Department d = new Department();
                    d.setName(name);
                    return d;
                })
                .collect(Collectors.toList());

        if (!newDepartments.isEmpty()) {
            List<Department> savedDepts = departmentRepository.saveAll(newDepartments);
            savedDepts.forEach(d -> departmentCache.put(d.getName(), d));
        }

        String hashedPassword = BCrypt.hashpw("trivik", BCrypt.gensalt(10));

        // Fetch existing employees from DB by empId
        Set<String> empIds = employees.stream()
                .map(Employee::getEmpId)
                .collect(Collectors.toSet());

        Map<String, EmployeeInformation> existingEmpMap = employeeInformationRepository.findByEmpIdIn(empIds).stream()
                .collect(Collectors.toMap(EmployeeInformation::getEmpId, e -> e));

        List<EmployeeInformation> toSave = new ArrayList<>();

        for (Employee emp : employees) {
            EmployeeInformation info = existingEmpMap.getOrDefault(emp.getEmpId(), new EmployeeInformation());

            // Set/update fields
            info.setEmpId(emp.getEmpId());
            info.setName(emp.getName());
            info.setBranch(emp.getBranch());
            info.setDob(emp.getDob());
            info.setCurrentDesignation(emp.getCurrentDesignation());
            info.setDateOfJoining(emp.getDateOfJoining());
            info.setEmailId(emp.getEmailId());
            info.setOfficialEmailId(emp.getOfficialEmailId());
            info.setMobileNumber(emp.getMobileNumber());
            info.setPassword(info.getEmpId() == null ? hashedPassword : info.getPassword()); // Set only if new
            info.setRole("Manager".equalsIgnoreCase(emp.getCategory()) ? "MANAGER" : "EMPLOYEE");
            info.setCategory(emp.getCategory());
            info.setReportingManager(emp.getReportingManager());
            info.setDepartment(departmentCache.get(emp.getDepartment()));

            toSave.add(info);
        }

        // Save updated or new employee information
        if (!toSave.isEmpty()) {
            employeeInformationRepository.saveAll(toSave);
        }

        // Count managers and employees
        long managerCount = toSave.stream()
                .filter(e -> "MANAGER".equalsIgnoreCase(e.getRole()))
                .count();

        long employeeCount = toSave.size() - managerCount;

        return List.of(
                "Managers Processed: " + managerCount,
                "Employees Processed: " + employeeCount);
    }

    @Override
    public List<EmployeeInfo> getAllEmployees() {
        List<EmployeeInformation> allRecords = employeeInformationRepository.findAll();

        // Convert all EmployeeInformation to EmployeeInfo
        return allRecords.parallelStream()
                .map(employee -> {
                    EmployeeInfo info = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);

                    // Set department name safely
                    if (employee.getDepartment() != null) {
                        info.setDepartment(employee.getDepartment().getName());
                    } else {
                        info.setDepartment("N/A");
                    }

                    // Set reporting manager safely
                    if (employee.getReportingManager() != null && !employee.getReportingManager().isEmpty()) {
                        info.setReportingManager(employee.getReportingManager());
                    } else {
                        info.setReportingManager("N/A");
                    }

                    return info;
                }).toList();
    }

    @Override
    public EmployeeInfo getEmployeeById(String employeeId) {
        EmployeeInformation employee = employeeInformationRepository.findById(employeeId).orElse(null);
        EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);
        employeeInfo.setReportingManager(employee.getReportingManager());
        employeeInfo.setDepartment(employee.getDepartment().getName());

        return employeeInfo;
    }

    @Override
    public Integer totalEmployees() {
        return employeeInformationRepository.findAll().size();
    }

    @Override
    public List<EmployeeInfo> searchEmployee(String search) {
        List<EmployeeInformation> employees = employeeInformationRepository.searchEmployees(search);

        // Convert EmployeeInformation to EmployeeInfo with null safety
        return employees.parallelStream().map(employee -> {
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);

            // Set department name safely
            if (employee.getDepartment() != null) {
                employeeInfo.setDepartment(employee.getDepartment().getName());
            } else {
                employeeInfo.setDepartment("N/A");
            }

            // Set reporting manager safely
            if (employee.getReportingManager() != null) {
                employeeInfo.setReportingManager(employee.getReportingManager());
            } else {
                employeeInfo.setReportingManager("N/A");
            }

            return employeeInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> deleteEmployee(String employeeId) {
        Map<String, String> response = new HashMap<>();
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
        if (employeeById.isPresent()) {
            EmployeeInformation employeeInformation = employeeById.get();
            employeeInformationRepository.delete(employeeInformation);
            response.put("message", "Employee deleted with ID  " + employeeId);
            return response;
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Override
    public EmployeeInfo updateEmployee(String empId, Employee employee) {
        Optional<EmployeeInformation> savedEmployee = employeeInformationRepository.findById(empId);

        if (savedEmployee.isPresent()) {
            EmployeeInformation employee1 = savedEmployee.get();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            // Convert date fields to String
            String lwdStr = employee.getLastWorkingDate() != null ? formatter.format(employee.getLastWorkingDate())
                    : null;

            // Set values
            employee1.setBranch(employee.getBranch());
            employee1.setCategory(employee.getCategory());
            employee1.setDob(employee.getDob());
            employee1.setName(employee.getName());
            employee1.setCurrentDesignation(employee.getCurrentDesignation());
            employee1.setDateOfJoining(employee.getDateOfJoining());
            employee1.setEmailId(employee.getEmailId());
            employee1.setLastWorkingDay(lwdStr);
            employee1.setMobileNumber(employee.getMobileNumber());
            employee1.setOfficialEmailId(employee.getOfficialEmailId());
            employee1.setReportingManager(employee.getReportingManager());

            // Set department name
            Department department = employee1.getDepartment();
            department.setName(employee.getDepartment());
            employee1.setDepartment(department);

            // Save and convert to DTO
            EmployeeInformation save = employeeInformationRepository.save(employee1);
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(save, EmployeeInfo.class);

            return employeeInfo;
        }

        throw new EmployeeNotFoundException(empId);
    }

    @Override
    public List<EmployeeWithPmsStatus> getCompletedPmsForHR() {
        return employeeInformationRepository.findAll().stream()
                .map(employee -> {
                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
                    if (kraKpiOptional.isEmpty())
                        return null;

                    KraKpi kraKpi = kraKpiOptional.get();

                    EmployeeWithPmsStatus dto = new EmployeeWithPmsStatus();
                    dto.setName(employee.getName());
                    dto.setOfficialEmailId(employee.getOfficialEmailId());
                    dto.setPmsInitiated(kraKpi.getPmsInitiated());
                    dto.setEmpId(employee.getEmpId());
                    dto.setCurrentDesignation(employee.getCurrentDesignation());
                    DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
                    departmentResponseDto.setName(employee.getDepartment().getName());
                    departmentResponseDto.setDepartmentId(employee.getDepartment().getDepartmentId());
                    dto.setDepartment(departmentResponseDto);
                    dto.setSelfCompleted(kraKpi.isSelfCompleted());
                    dto.setManagerCompleted(kraKpi.isManagerCompleted());

                    if (!Boolean.TRUE.equals(kraKpi.getPmsInitiated()) ||
                            !(kraKpi.isSelfCompleted() && kraKpi.isManagerCompleted())) {
                        return null;
                    }

                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<EmployeeWithPmsStatus> getPendingPmsForHR() {
        return employeeInformationRepository.findAll().stream()
                .map(employee -> {
                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
                    if (kraKpiOptional.isEmpty())
                        return null;

                    KraKpi kraKpi = kraKpiOptional.get();

                    EmployeeWithPmsStatus dto = new EmployeeWithPmsStatus();
                    dto.setName(employee.getName());
                    dto.setOfficialEmailId(employee.getOfficialEmailId());
                    dto.setPmsInitiated(kraKpi.getPmsInitiated());
                    dto.setEmpId(employee.getEmpId());
                    dto.setCurrentDesignation(employee.getCurrentDesignation());
                    DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
                    departmentResponseDto.setName(employee.getDepartment().getName());
                    departmentResponseDto.setDepartmentId(employee.getDepartment().getDepartmentId());
                    dto.setDepartment(departmentResponseDto);
                    dto.setSelfCompleted(kraKpi.isSelfCompleted());
                    dto.setManagerCompleted(kraKpi.isManagerCompleted());

                    if (!Boolean.TRUE.equals(kraKpi.getPmsInitiated()) ||
                            (kraKpi.isSelfCompleted() && kraKpi.isManagerCompleted())) {
                        return null;
                    }

                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public PmsPercentageDto getPmsPercentageForHR() {
        List<EmployeeInformation> employees = employeeInformationRepository.findAll();

        long totalInitiated = 0;
        long completedCount = 0;

        for (EmployeeInformation employee : employees) {
            Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
            if (kraKpiOptional.isEmpty())
                continue;

            KraKpi kraKpi = kraKpiOptional.get();

            // Only include if PMS is initiated
            if (!Boolean.TRUE.equals(kraKpi.getPmsInitiated()))
                continue;

            totalInitiated++;

            // Count as completed only if both self and manager parts are done
            if (kraKpi.isSelfCompleted() && kraKpi.isManagerCompleted()) {
                completedCount++;
            }
        }

        double completedPercentage = totalInitiated > 0
                ? (completedCount * 100.0) / totalInitiated
                : 0.0;

        double pendingPercentage = 100.0 - completedPercentage;

        return new PmsPercentageDto(completedPercentage, pendingPercentage);
    }

    @Override
    public List<EmployeeInfo> employeesWithKraKpiApproval() {
        List<EmployeeInformation> allEmployees = employeeInformationRepository.findAll();

        return allEmployees.stream()
                .map(employee -> {
                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);

                    if (kraKpiOptional.isPresent() && Boolean.TRUE.equals(kraKpiOptional.get().getManagerApproval())) {
                        EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee,
                                EmployeeInfo.class);
                        employeeInfo.setDepartment(employee.getDepartment().getName());
                        return employeeInfo;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> initiatePms(String employeeId, Map<String, Boolean> pms) {
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
        if (employeeById.isPresent()) {
            EmployeeInformation employee = employeeById.get();
            Optional<KraKpi> krakpi = kraKpiRepository.findByEmployeeInformation(employee);
            if (krakpi.isPresent()) {
                KraKpi kraKpi = krakpi.get();
                kraKpi.setPmsInitiated(pms.get("pms_initiated"));
                kraKpiRepository.save(kraKpi);
            } else {
                throw new KraKpiNotFoundException("Kra Kpi not found for employee with id " + employeeId);
            }
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
        return Map.of("status", "Pms Initiated successfully");
    }

    @Override
    public List<Long> getEmployeeCountByDepartment() {
        List<Object[]> result = employeeInformationRepository.countEmployeesByDepartment();

        return result.stream()
                .map(row -> (Long) row[1]) // row[1] is the count
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> assessmentKeyMatrix() {
        Map<Boolean, Long> result = employeeInformationRepository.findAll()
                .stream()
                .map(kraKpiRepository::findByEmployeeInformation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.partitioningBy(
                        kraKpi -> kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted(),
                        Collectors.counting()));
        return Map.of(
                "completed", result.getOrDefault(true, 0L).intValue(),
                "pending", result.getOrDefault(false, 0L).intValue());
    }

    @Override
    public List<EmployeeInfo> pmsInitiatedEmployees() {
        return employeeInformationRepository.findAll().stream()
                .map(employee -> {
                    Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
                    if (kraKpiOptional.isPresent()) {
                        KraKpi kraKpi = kraKpiOptional.get();
                        boolean managerApproval = Boolean.TRUE.equals(kraKpi.getManagerApproval());
                        boolean selfCompleted = kraKpi.isSelfCompleted();
                        boolean managerCompleted = kraKpi.isManagerCompleted();

                        // Include if managerApproval is true and NOT (selfCompleted &&
                        // managerCompleted)
                        if (managerApproval && !(selfCompleted && managerCompleted)) {
                            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee,
                                    EmployeeInfo.class);
                            employeeInfo.setDepartment(employee.getDepartment().getName());
                            return employeeInfo;
                        }
                    }
                    return null; // Skip this employee
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, String> employeeRegistration(Employee employee) {
        // Creating response object
        Map<String, String> response = new HashMap<>();

        // Converting Employee Dto class to EmployeeInformation entity class
        EmployeeInformation employeeInformation = entityDtoConversion.dtoToEntityConversion(employee,
                EmployeeInformation.class);
        Department department = departmentRepository.findByName(employee.getDepartment());
        employeeInformation.setDepartment(department);

        // converting date into string
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String lwdStr = employee.getLastWorkingDate() != null ? formatter.format(employee.getLastWorkingDate()) : null;
        employeeInformation.setLastWorkingDay(lwdStr);

        EmployeeInformation saved = employeeInformationRepository.save(employeeInformation);

        try {
            String subject = String.format(Message.REGISTRATION_SUBJECT, saved.getName());
            String message = String.format(Message.REGISTRATION_MESSAGE, saved.getName(), new Date(), saved.getEmpId());
            emailService.sendEmail(saved.getEmailId(), subject, message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String mesg = saved != null ? "Employee Registration Successful" : "Employee Registration Failed";

        response.put("status", mesg);
        return response;
    }

    @Override
    public Map<String, Object> getdepartment() {
        List<Department> allDpt = departmentRepository.findAll();

        List<String> departmentNames = allDpt.stream()
                .map(Department::getName)
                .collect(Collectors.toList());
        return Map.of("departments", departmentNames);

    }

}