package com.triviktech.services.hr;

import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.krakpi.KraKpi;
import com.triviktech.entities.manager.Manager;
import com.triviktech.exception.employee.EmployeeAlreadyExistsException;
import com.triviktech.exception.employee.EmployeeNotFoundException;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.exception.krakpi.KraKpiNotFoundException;
import com.triviktech.exception.manager.ManagerAlreadyExistsException;
import com.triviktech.exception.validation.InvalidEmailIdException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.request.manager.ManagerDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.utilities.email.EmailService;
import com.triviktech.utilities.email.Message;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import com.triviktech.utilities.validation.Validation;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final ManagerRepository managerRepository;

    public HrServiceImpl(HRRepository hrRepository, StateRepository stateRepository,
                         LocationRepository locationRepository, DepartmentRepository departmentRepository,
                         KraKpiRepository kraKpiRepository,
                         EntityDtoConversion entityDtoConversion, EmployeeInformationRepository employeeInformationRepository, EmailService emailService, ManagerRepository managerRepository) {

        this.hrRepository = hrRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.emailService = emailService;
        this.entityDtoConversion = entityDtoConversion;
        this.employeeInformationRepository = employeeInformationRepository;
        this.kraKpiRepository = kraKpiRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public HrResponseDto registerHr(HrRequestDto hrRequestDto) {
        return null;
    }

    @Override
    public HrResponseDto findHrById(String hrId) {
        return null;
    }

    @Override
    public List<Object> uploadEmployeesData(MultipartFile file) throws IOException {
        List<Employee> employees = XlsxSupport.convertXlsxToListOfEmployee(file.getInputStream());

        // Step 1: Filter valid rows
        employees = employees.stream()
                .filter(emp -> emp != null &&
                        emp.getName() != null && !emp.getName().trim().isEmpty() &&
                        emp.getDepartment() != null && !emp.getDepartment().trim().isEmpty())
                .collect(Collectors.toList());

        // Step 2: Cache departments
        Map<String, Department> departmentCache = departmentRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Department::getName, d -> d));

        Set<String> allDeptNames = employees.stream()
                .map(Employee::getDepartment)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toSet());

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

        Set<String> empIds = employees.stream()
                .map(Employee::getEmpId)
                .collect(Collectors.toSet());

        Map<String, EmployeeInformation> existingEmpMap = employeeInformationRepository.findByEmpIdIn(empIds).stream()
                .collect(Collectors.toMap(EmployeeInformation::getEmpId, e -> e));

        Map<String, Manager> existingManagerMap = managerRepository.findByManagerIdIn(empIds).stream()
                .collect(Collectors.toMap(Manager::getManagerId, m -> m));

        Map<String, Manager> allManagersByName = managerRepository.findAll().stream()
                .collect(Collectors.toMap(
                        m -> m.getName().trim().toLowerCase(),
                        m -> m,
                        (m1, m2) -> m1
                ));

        // Step 3: Identify managers
        Set<String> reportingManagerNames = employees.stream()
                .map(Employee::getReportingManager)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<Employee> managerRecords = employees.stream()
                .filter(emp -> reportingManagerNames.contains(emp.getName().trim().toLowerCase()))
                .collect(Collectors.toList());

        Set<String> managerEmpIds = managerRecords.stream()
                .map(Employee::getEmpId)
                .collect(Collectors.toSet());

        // Step 4: Save only managers in manager table
        Map<String, Manager> managerByEmpId = new HashMap<>(existingManagerMap);
        List<Manager> toSaveManagers = new ArrayList<>();

        for (Employee emp : managerRecords) {
            Manager manager = managerByEmpId.getOrDefault(emp.getEmpId(), new Manager());
            manager.setManagerId(emp.getEmpId());
            manager.setName(emp.getName());
            manager.setBranch(emp.getBranch());
            manager.setDob(emp.getDob());
            manager.setCurrentDesignation(emp.getCurrentDesignation());
            manager.setDateOfJoining(emp.getDateOfJoining());
            manager.setEmailId(emp.getEmailId());
            manager.setOfficialEmailId(emp.getOfficialEmailId());
            manager.setMobileNumber(emp.getMobileNumber());
            manager.setPassword(hashedPassword);
            manager.setRole("MANAGER");
            manager.setCategory(emp.getCategory());
            manager.setDepartment(departmentCache.get(emp.getDepartment()));
            manager.setReportingManager(emp.getReportingManager() != null ? emp.getReportingManager().trim() : null);

            managerByEmpId.put(emp.getEmpId(), manager);
            toSaveManagers.add(manager);
        }

        if (!toSaveManagers.isEmpty()) {
            List<Manager> savedManagers = managerRepository.saveAll(toSaveManagers);
            for (Manager m : savedManagers) {
                allManagersByName.put(m.getName().trim().toLowerCase(), m);
                managerByEmpId.put(m.getManagerId(), m);
            }
        }

        // Step 5: Prepare employees (excluding those who are managers)
        List<EmployeeInformation> toSaveEmployees = new ArrayList<>();
        for (Employee emp : employees) {
            if (managerEmpIds.contains(emp.getEmpId())) {
                continue; // skip if already saved as manager
            }

            Manager reportingManagerObj = null;
            if (emp.getReportingManager() != null && !emp.getReportingManager().trim().isEmpty()) {
                reportingManagerObj = allManagersByName.get(emp.getReportingManager().trim().toLowerCase());
            }

            EmployeeInformation info = existingEmpMap.getOrDefault(emp.getEmpId(), new EmployeeInformation());
            info.setEmpId(emp.getEmpId());
            info.setName(emp.getName());
            info.setBranch(emp.getBranch());
            info.setDob(emp.getDob());
            info.setCurrentDesignation(emp.getCurrentDesignation());
            info.setDateOfJoining(emp.getDateOfJoining());
            info.setEmailId(emp.getEmailId());
            info.setOfficialEmailId(emp.getOfficialEmailId());
            info.setMobileNumber(emp.getMobileNumber());
            info.setPassword(hashedPassword);
            info.setRole("EMPLOYEE");
            info.setCategory(emp.getCategory());
            info.setDepartment(departmentCache.get(emp.getDepartment()));
            if (reportingManagerObj != null) {
                info.setManager(reportingManagerObj);
            }

            toSaveEmployees.add(info);
        }

        if (!toSaveEmployees.isEmpty()) {
            employeeInformationRepository.saveAll(toSaveEmployees);
        }

        return List.of(
                "Managers Processed: " + toSaveManagers.size(),
                "Employees Processed: " + toSaveEmployees.size()
        );
    }

    @Override
    public List<EmployeeInfo> getAllEmployees() {
        List<EmployeeInformation> employees = employeeInformationRepository.findAll();
        List<Manager> managers = managerRepository.findAll();

        List<EmployeeInfo> employeeDtos = employees.parallelStream().map(employee -> {
            EmployeeInfo dto = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);

            dto.setDepartment(
                    employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A"
            );

            dto.setReportingManager(
                    employee.getManager() != null ? employee.getManager().getName() : "N/A"
            );

            return dto;
        }).collect(Collectors.toList());

        List<EmployeeInfo> managerDtos = managers.parallelStream().map(manager -> {
            EmployeeInfo dto = entityDtoConversion.entityToDtoConversion(manager, EmployeeInfo.class);
            dto.setEmpId(manager.getManagerId());

            dto.setDepartment(
                    manager.getDepartment() != null ? manager.getDepartment().getName() : "N/A"
            );

            dto.setReportingManager(
                    manager.getReportingManager() != null ? manager.getReportingManager() : "N/A"
            );

            return dto;
        }).collect(Collectors.toList());

        List<EmployeeInfo> combinedList = new ArrayList<>();
        combinedList.addAll(employeeDtos);
        combinedList.addAll(managerDtos);

        return combinedList;
    }

    @Override
    public EmployeeInfo getEmployeeById(String employeeId) {
        // First check in employee table
        Optional<EmployeeInformation> optionalEmp = employeeInformationRepository.findById(employeeId);
        if (optionalEmp.isPresent()) {
            EmployeeInformation employee = optionalEmp.get();
            EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);

            // Safely set manager name
            if (employee.getManager() != null) {
                employeeInfo.setReportingManager(employee.getManager().getName());
            } else {
                employeeInfo.setReportingManager("N/A");
            }

            // Safely set department name
            if (employee.getDepartment() != null) {
                employeeInfo.setDepartment(employee.getDepartment().getName());
            } else {
                employeeInfo.setDepartment("N/A");
            }

            return employeeInfo;
        }

        // If not found in employee, check in manager table
        Optional<Manager> optionalManager = managerRepository.findById(employeeId);
        if (optionalManager.isPresent()) {
            Manager manager = optionalManager.get();
            EmployeeInfo managerInfo = entityDtoConversion.entityToDtoConversion(manager, EmployeeInfo.class);
            managerInfo.setEmpId(manager.getManagerId());

            // For manager's reporting manager
            if (manager.getReportingManager() != null && !manager.getReportingManager().isBlank()) {
                managerInfo.setReportingManager(manager.getReportingManager());
            } else {
                managerInfo.setReportingManager("N/A");
            }

            // For manager's department
            if (manager.getDepartment() != null) {
                managerInfo.setDepartment(manager.getDepartment().getName());
            } else {
                managerInfo.setDepartment("N/A");
            }

            return managerInfo;
        }

        // If not found in either
        throw new EntityNotFoundException("No employee or manager found with ID: " + employeeId);
    }

    @Override
    public Integer totalEmployees() {
        return employeeInformationRepository.findAll().size()+managerRepository.findAll().size();
    }

    @Override
    public List<EmployeeInfo> searchEmployee(String search) {
        String searchLower = search.trim().toLowerCase();

        List<EmployeeInformation> employees = employeeInformationRepository.searchEmployees(search);
        List<Manager> managers = managerRepository.searchManagers(search);

        List<EmployeeInfo> employeeDtos = employees.stream()
                .map(employee -> {
                    EmployeeInfo dto = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);

                    dto.setEmpId(employee.getEmpId());
                    dto.setName(employee.getName());
                    dto.setDepartment(employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A");
                    dto.setReportingManager(employee.getManager() != null ? employee.getManager().getName() : "N/A");
                    dto.setCategory(employee.getCategory());
                    dto.setRole("EMPLOYEE");

                    return dto;
                }).collect(Collectors.toList());

        List<EmployeeInfo> managerDtos = managers.stream()
                .map(manager -> {
                    EmployeeInfo dto = entityDtoConversion.entityToDtoConversion(manager, EmployeeInfo.class);

                    dto.setEmpId(manager.getManagerId());
                    dto.setName(manager.getName());
                    dto.setDepartment(manager.getDepartment() != null ? manager.getDepartment().getName() : "N/A");
                    dto.setReportingManager(manager.getReportingManager() != null ? manager.getReportingManager() : "N/A");
                    dto.setCategory(manager.getCategory());
                    dto.setRole("MANAGER");

                    return dto;
                }).collect(Collectors.toList());

        List<EmployeeInfo> combinedList = new ArrayList<>();
        combinedList.addAll(employeeDtos);
        combinedList.addAll(managerDtos);

        // Sort by priority (startsWith → contains → else) and then by name
        combinedList.sort((a, b) -> {
            int priorityA = getPriorityScore(a, searchLower);
            int priorityB = getPriorityScore(b, searchLower);

            if (priorityA != priorityB) {
                return Integer.compare(priorityA, priorityB);
            } else {
                // Secondary sort by name alphabetically
                String nameA = a.getName() != null ? a.getName().toLowerCase() : "";
                String nameB = b.getName() != null ? b.getName().toLowerCase() : "";
                return nameA.compareTo(nameB);
            }
        });

        return combinedList;
    }

    private int getPriorityScore(EmployeeInfo info, String searchLower) {
        String name = safe(info.getName());
        String dept = safe(info.getDepartment());
        String role = safe(info.getRole());
        String category = safe(info.getCategory());
        String manager = safe(info.getReportingManager());

        if (name.startsWith(searchLower) || dept.startsWith(searchLower) || role.startsWith(searchLower)
                || category.startsWith(searchLower) || manager.startsWith(searchLower)) {
            return 1;
        } else if (name.contains(searchLower) || dept.contains(searchLower) || role.contains(searchLower)
                || category.contains(searchLower) || manager.contains(searchLower)) {
            return 2;
        }
        return 3;
    }

    private String safe(String val) {
        return val != null ? val.toLowerCase() : "";
    }

    @Override
    public Map<String, String> deleteEmployee(String employeeId) {
        Map<String, String> response = new HashMap<>();
        Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
        Optional<Manager> optionalManager = managerRepository.findByManagerId(employeeId);
        if (employeeById.isPresent()) {
            EmployeeInformation employeeInformation = employeeById.get();
            employeeInformationRepository.delete(employeeInformation);
            response.put("message", "Employee deleted with ID  " + employeeId);
            return response;
        } else if (optionalManager.isPresent()) {
            Manager manager = optionalManager.get();
            managerRepository.delete(manager);
            response.put("message", "Employee deleted with ID  " + employeeId);
            return response;

        } else {
            throw new EmployeeNotFoundException(employeeId);
        }

    }

    @Override
    public Map<String, String> updateEmployee(String empId, Employee employee) {
        Map<String,String> response=new HashMap<>();

        Optional<EmployeeInformation> savedEmployee = employeeInformationRepository.findById(empId);
        Optional<Manager> optionalManager = managerRepository.findByManagerId(empId);

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
            employee1.setRole(employee.getRole().toUpperCase());

            // Set department name
            Department department = employee1.getDepartment();
            if (department == null) {
                department = departmentRepository.findByName(employee.getDepartment());
            } else {
                department.setName(employee.getDepartment());
            }
            employee1.setDepartment(department);

            // Set reporting manager (now a Manager object)
            Manager reportingManagerObj = null;
            if (employee.getReportingManager() != null && !employee.getReportingManager().trim().isEmpty()) {
                reportingManagerObj = managerRepository.findByName(employee.getReportingManager()).get();
            }
            employee1.setManager(reportingManagerObj);

            // Save and convert to DTO
            EmployeeInformation save = employeeInformationRepository.save(employee1);

            try{
                String sub=String.format(Message.EMPLOYEE_DETAILS_UPDATED_SUBJECT);
                String message=String.format(Message.EMPLOYEE_DETAILS_UPDATED_MESSAGE,save.getName());
                emailService.sendEmail(save.getEmailId(),sub,message);
            }catch (Exception e){
                e.printStackTrace();
            }
            response.put("Status","Employee Information Updated Successfully");

            return response;
        }
        if(optionalManager.isPresent()){
            Manager manager = optionalManager.get();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            // Convert date fields to String
//            String lwdStr = employee.getLastWorkingDate() != null ? formatter.format(employee.getLastWorkingDate()) : null;

            // Set values
            manager.setBranch(employee.getBranch());
            manager.setCategory(employee.getCategory());
            manager.setDob(employee.getDob());
            manager.setName(employee.getName());
            manager.setCurrentDesignation(employee.getCurrentDesignation());
            manager.setDateOfJoining(employee.getDateOfJoining());
            manager.setEmailId(employee.getEmailId());
//            manager.setLastWorkingDate(lwdStr);
            manager.setMobileNumber(employee.getMobileNumber());
            manager.setOfficialEmailId(employee.getOfficialEmailId());
            manager.setRole(employee.getRole());

            // Set department name
            Department department = manager.getDepartment();
            if (department == null) {
                department = departmentRepository.findByName(employee.getDepartment());
            } else {
                department.setName(employee.getDepartment());
            }
            manager.setDepartment(department);

            manager.setReportingManager(employee.getReportingManager());

            // Save and convert to DTO
            Manager saved = managerRepository.save(manager);

            try{
                String sub=String.format(Message.EMPLOYEE_DETAILS_UPDATED_SUBJECT);
                String message=String.format(Message.EMPLOYEE_DETAILS_UPDATED_MESSAGE,saved.getName());
                emailService.sendEmail(saved.getEmailId(),sub,message);
            }catch (Exception e){
                e.printStackTrace();
            }

            response.put("Status","Employee Information Updated Successfully");

            return response;

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
    public HrResponseDto profile(String hrId) {
        Optional<HR> byId = hrRepository.findById(hrId);
        if(byId.isEmpty()){
            throw new HRNotFoundException(hrId);
        }
        HR hr = byId.get();
        HrResponseDto response = entityDtoConversion.entityToDtoConversion(hr, HrResponseDto.class);
        response.setDepartment(hr.getDepartment().getName());
        return response;
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
                        if (employee.getDepartment() != null) {
                            employeeInfo.setDepartment(employee.getDepartment().getName());
                        } else {
                            employeeInfo.setDepartment("N/A");
                        }
                        // reporting manager
                        if (employee.getManager() != null) {
                            employeeInfo.setReportingManager(employee.getManager().getName());
                        } else {
                            employeeInfo.setReportingManager("N/A");
                        }
                        return employeeInfo;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

   @Override
public Map<String, String> initiatePms(String employeeId, Map<String, Boolean> pms) {
    Map<String, String> response = new HashMap<>();

    Optional<EmployeeInformation> employeeById = employeeInformationRepository.findById(employeeId);
    if (employeeById.isEmpty()) {
        response.put("status", "failure");
        response.put("message", "Employee not found");
        return response;
    }

    EmployeeInformation employee = employeeById.get();

    Optional<KraKpi> krakpi = kraKpiRepository.findByEmployeeInformation(employee);
    if (krakpi.isEmpty()) {
        response.put("status", "failure");
        response.put("message", "KRA/KPI not found for this employee");
        return response;
    }

    KraKpi kraKpi = krakpi.get();
    kraKpi.setPmsInitiated(pms.getOrDefault("pms_initiated", false));
    kraKpiRepository.save(kraKpi);

    try {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = futureDate.format(formatter);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);

        String duration = startDate.format(formatter) + " to " + endDate.format(formatter);
        String submissionDeadline = formattedDate;

        String sub = String.format(Message.PMS_SUBJECT, formattedDate);
        String message = String.format(Message.PMS_MESSAGE, employee.getName(), duration, submissionDeadline);
        emailService.sendEmail(employee.getEmailId(), sub, message);

    } catch (Exception e) {
        e.printStackTrace();
        // Optional: don't fail whole process due to email failure
    }

    response.put("status", "success");
    response.put("message", "PMS initiated successfully");
    return response;
}
    @Override
    public List<Long> getEmployeeCountByDepartment() {
        // Get employee counts by department
        List<Object[]> empResult = employeeInformationRepository.countEmployeesByDepartment();
        // Get manager counts by department
        List<Object[]> mgrResult = managerRepository.countManagersByDepartment();

        // Merge employee and manager department counts: Map<dept, count>
        Map<String, Long> deptCountMap = new HashMap<>();
        for (Object[] row : empResult) {
            String dept = (String) row[0];
            Long count = (Long) row[1];
            deptCountMap.put(dept, count);
        }
        for (Object[] row : mgrResult) {
            String dept = (String) row[0];
            Long count = (Long) row[1];
            deptCountMap.put(dept, deptCountMap.getOrDefault(dept, 0L) + count);
        }

        return new ArrayList<>(deptCountMap.values());
    }

    @Override
    public Map<String, Integer> assessmentKeyMatrix() {
        // Employees
        Map<Boolean, Long> empResult = employeeInformationRepository.findAll()
                .stream()
                .map(kraKpiRepository::findByEmployeeInformation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.partitioningBy(
                        kraKpi -> kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted(),
                        Collectors.counting()));

        // Managers (for those who also have KraKpi)
        Map<Boolean, Long> mgrResult = managerRepository.findAll()
                .stream()
                .map(kraKpiRepository::findByManager)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.partitioningBy(
                        kraKpi -> kraKpi.isManagerCompleted() && kraKpi.isSelfCompleted(),
                        Collectors.counting()));

        long completed = empResult.getOrDefault(true, 0L) + mgrResult.getOrDefault(true, 0L);
        long pending = empResult.getOrDefault(false, 0L) + mgrResult.getOrDefault(false, 0L);

        return Map.of(
                "completed", (int) completed,
                "pending", (int) pending
        );
    }

    @Override
    public List<EmployeeInfo> pmsInitiatedEmployees() {
        List<EmployeeInfo> result = new ArrayList<>();

        // Employees
        employeeInformationRepository.findAll().forEach(employee -> {
            Optional<KraKpi> kraKpiOptional = kraKpiRepository.findByEmployeeInformation(employee);
            if (kraKpiOptional.isPresent()) {
                KraKpi kraKpi = kraKpiOptional.get();
                boolean managerApproval = Boolean.TRUE.equals(kraKpi.getManagerApproval());
                boolean selfCompleted = kraKpi.isSelfCompleted();
                boolean managerCompleted = kraKpi.isManagerCompleted();
                if (managerApproval && !(selfCompleted && managerCompleted)) {
                    EmployeeInfo employeeInfo = entityDtoConversion.entityToDtoConversion(employee, EmployeeInfo.class);
                    if (employee.getDepartment() != null) {
                        employeeInfo.setDepartment(employee.getDepartment().getName());
                    } else {
                        employeeInfo.setDepartment("N/A");
                    }
                    if (employee.getManager() != null) {
                        employeeInfo.setReportingManager(employee.getManager().getName());
                    } else {
                        employeeInfo.setReportingManager("N/A");
                    }
                    result.add(employeeInfo);
                }
            }
        });

        return result;
    }

    @Override
    public Map<String, String> employeeRegistration(Employee employee) {
        Map<String, String> response = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Optional<EmployeeInformation> employeeByEmail = employeeInformationRepository.findByEmailId(employee.getEmailId());

        if(employeeByEmail.isPresent()){
            throw new EmployeeAlreadyExistsException(employee.getEmailId());
        }

        Optional<EmployeeInformation> employeeBYId = employeeInformationRepository.findById(employee.getEmpId());
        if(employeeBYId.isPresent()){
            throw new EmployeeAlreadyExistsException(employee.getEmpId());
        }

        Optional<Manager> byEmailId = managerRepository.findByEmailId(employee.getEmailId());
        if(byEmailId.isPresent()){
            throw new ManagerAlreadyExistsException(employee.getEmailId());
        }

        Optional<Manager> managerById = managerRepository.findByManagerId(employee.getEmpId());
        if(managerById.isPresent()){
            throw new ManagerAlreadyExistsException(employee.getEmpId());
        }

        if (employee.getRole().equalsIgnoreCase("EMPLOYEE")) {
            EmployeeInformation employeeInformation = entityDtoConversion.dtoToEntityConversion(employee, EmployeeInformation.class);

            Department department = departmentRepository.findByName(employee.getDepartment());
            employeeInformation.setDepartment(department);

            String lwdStr = employee.getLastWorkingDate() != null ? formatter.format(employee.getLastWorkingDate()) : null;
            employeeInformation.setLastWorkingDay(lwdStr);

            // Set Manager
            if (employee.getReportingManager() != null && !employee.getReportingManager().trim().isEmpty()) {
                Optional<Manager> optionalManager = managerRepository.findByName(employee.getReportingManager());
                optionalManager.ifPresent(employeeInformation::setManager);
            }

            // Set HR
            if (employee.getHrId() != null && !employee.getHrId().trim().isEmpty()) {
                Optional<HR> optionalHR = hrRepository.findById(employee.getHrId());
                optionalHR.ifPresent(employeeInformation::sethR);
            }

            String hashedPassword = BCrypt.hashpw("trivik", BCrypt.gensalt(10));
            employeeInformation.setPassword(hashedPassword);

            if(!Validation.emailValidation(employee.getEmailId())){
                throw new InvalidEmailIdException("Invalid email id");
            }
            if(!Validation.emailValidation(employee.getOfficialEmailId())){
                throw new InvalidEmailIdException("Invalid email id");
            }

            EmployeeInformation saved = employeeInformationRepository.save(employeeInformation);

            try {
                String subject = String.format(Message.REGISTRATION_SUBJECT, saved.getName());
                String message = String.format(Message.REGISTRATION_MESSAGE, saved.getName(), new Date(), saved.getEmpId());
                emailService.sendEmail(saved.getEmailId(), subject, message);
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.put("status", saved != null ? "Employee Registration Successful" : "Employee Registration Failed");
            return response;
        }

        if (employee.getRole().equalsIgnoreCase("MANAGER") || employee.getCategory().equalsIgnoreCase("MANAGER")) {
            Manager manager = entityDtoConversion.dtoToEntityConversion(employee, Manager.class);
            manager.setManagerId(employee.getEmpId());
            Department department = departmentRepository.findByName(employee.getDepartment());
            manager.setDepartment(department);

            String lwdStr = employee.getLastWorkingDate() != null ? formatter.format(employee.getLastWorkingDate()) : null;

            // Set Reporting Manager
            if (employee.getReportingManager() != null && !employee.getReportingManager().trim().isEmpty()) {
                Optional<Manager> reportingManagerOpt = managerRepository.findByName(employee.getReportingManager());
                reportingManagerOpt.ifPresent(rm -> manager.setReportingManager(rm.getName()));
            }

            // Set HR
            if (employee.getHrId() != null && !employee.getHrId().trim().isEmpty()) {
                Optional<HR> hrOpt = hrRepository.findById(employee.getHrId());
                hrOpt.ifPresent(manager::setHR);
            }

            if(!Validation.emailValidation(employee.getEmailId())){
                throw new InvalidEmailIdException("Invalid email id");
            }
            if(!Validation.emailValidation(employee.getOfficialEmailId())){
                throw new InvalidEmailIdException("Invalid email id");
            }

            Manager saved = managerRepository.save(manager);

            try {
                String sub = String.format(Message.REGISTRATION_SUBJECT, saved.getName());
                String message = String.format(Message.REGISTRATION_MESSAGE, saved.getName(), new Date(), saved.getManagerId());
                emailService.sendEmail(manager.getEmailId(), sub, message);
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.put("status", saved != null ? "Employee Registration Successful" : "Employee Registration Failed");
            return response;
        }

        response.put("status", "Something went wrong during the registration process");
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