package com.triviktech.services.hr;

import com.triviktech.entities.address.Country;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.employee.EmployeeInformation;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employeeslist.EmployeesList;
import com.triviktech.payloads.response.global.Response;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.payloads.response.manager.ManagerInfo;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class HrServiceImpl implements HrService{

    private final HRRepository hrRepository;
    private final StateRepository stateRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    private final EntityDtoConversion entityDtoConversion;
    private final EmployeeInformationRepository employeeInformationRepository;


    public HrServiceImpl(HRRepository hrRepository, StateRepository stateRepository, LocationRepository locationRepository, DepartmentRepository departmentRepository, ProjectRepository projectRepository, ManagerRepository managerRepository, ModelMapper modelMapper, EntityDtoConversion entityDtoConversion, EmployeeInformationRepository employeeInformationRepository) {
        this.hrRepository = hrRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.managerRepository = managerRepository;
        this.modelMapper = modelMapper;
        this.entityDtoConversion = entityDtoConversion;
        this.employeeInformationRepository = employeeInformationRepository;
    }

    @Override
    public HrResponseDto registerHr(HrRequestDto hrRequestDto) {

//        HR hr=new HR();
//        hr.setHrId(hrRequestDto.getHrId());
//        hr.setFirstName(hrRequestDto.getFirstName());
//        hr.setLastName(hrRequestDto.getLastName());
//        hr.setEmail(hrRequestDto.getEmail());
//        hr.setContactNumber(hrRequestDto.getContactNumber());
//        hr.setDateOfBirth(hrRequestDto.getDateOfBirth());
//        hr.setRole(hrRequestDto.getRole());
//        hr.setPassword(BCrypt.hashpw(hrRequestDto.getPassword(),BCrypt.gensalt(10)));
//
//        Location location=new Location();
//        location.setName(hrRequestDto.getLocationName());
//        location.setZipCode(hrRequestDto.getZipCode());
//        location.setState(stateRepository.findById(hrRequestDto.getStateId())
//                .orElseThrow(()-> new StateNotFoundException(hrRequestDto.getStateId())));
//
//        Location savedLocation = locationRepository.save(location);
//        hr.setLocation(savedLocation);
//
//        Set<Department> departments=new HashSet<>();
//        departments.add(departmentRepository.findById(hrRequestDto.getDepartmentId())
//                .orElseThrow(()-> new DepartmentNotFoundException(hrRequestDto.getDepartmentId())));
//        hr.setDepartments(departments);
//
//        Set<Project> projects=new HashSet<>();
//        projects.add((projectRepository.findById(hrRequestDto.getProjectId())
//                .orElseThrow(()-> new ProjectNotFoundException(hrRequestDto.getProjectId()))));
//        hr.setProjects(projects);
//
//        Set<Manager> managers=new HashSet<>();
//        managers.add(managerRepository.findById(hrRequestDto.getManagerId())
//                .orElseThrow(()-> new ManagerNotFoundException(hrRequestDto.getManagerId())));
//        hr.setManagers(managers);
//
//        HR saved = hrRepository.save(hr);
//        HrResponseDto hrResponseDto = mapToHrResponseDto(saved);
//
//        CountryResponseDto countryResponseDto = mapToCountryResponseDto(saved.getLocation().getState().getCountry());
//        StateResponseDto stateResponseDto = mapToStateResponseDto(saved.getLocation().getState());
//        LocationResponseDto locationResponseDto = mapToLocationResponseDto(savedLocation);
//        stateResponseDto.setCountry(countryResponseDto);
//        locationResponseDto.setState(stateResponseDto);
//        hrResponseDto.setLocationResponseDto(locationResponseDto);
//
//        Set<DepartmentResponseDto> departmentsDto = saved.getDepartments().stream().map(this::mapToDepartmentResponseDto).collect(Collectors.toSet());
//
//        hrResponseDto.setDepartments(departmentsDto);
//
//        Set<ProjectResponseDto> projectResponseDtos = saved.getProjects().stream().map(this::mapToProjectResponseDto).collect(Collectors.toSet());
//        hrResponseDto.setProjects(projectResponseDtos);
//
//        Set<ManagerResponseDto> managerResponseDtos = saved.getManagers().stream().map(this::mapToManagerResponseDto).collect(Collectors.toSet());
//
//        hrResponseDto.setManagers(managerResponseDtos);

//        return hrResponseDto;
return null;

    }

    @Override
    public HrResponseDto findHrById(String hrId) {

        HR hr = hrRepository.findById(hrId).orElseThrow(() -> new HRNotFoundException(hrId));

        HrResponseDto hrResponseDto = mapToHrResponseDto(hr);

        CountryResponseDto countryResponseDto = mapToCountryResponseDto(hr.getLocation().getState().getCountry());
        StateResponseDto stateResponseDto = mapToStateResponseDto(hr.getLocation().getState());
        LocationResponseDto locationResponseDto = mapToLocationResponseDto(hr.getLocation());
        stateResponseDto.setCountry(countryResponseDto);
        locationResponseDto.setState(stateResponseDto);
        hrResponseDto.setLocationResponseDto(locationResponseDto);

//        Set<DepartmentResponseDto> departmentsDto = hr.getDepartments().stream().map(this::mapToDepartmentResponseDto).collect(Collectors.toSet());

//        System.out.println(hr.getDepartments());
//        hrResponseDto.setDepartments(departmentsDto);

        Set<ProjectResponseDto> projectResponseDtos = hr.getProjects().stream().map(this::mapToProjectResponseDto).collect(Collectors.toSet());
        hrResponseDto.setProjects(projectResponseDtos);

//        Set<ManagerResponseDto> managerResponseDtos = hr.getManagers().stream().map(this::mapToManagerResponseDto).collect(Collectors.toSet());

//        hrResponseDto.setManagers(managerResponseDtos);

        return hrResponseDto;
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

        // Save all employees including managers into EmployeeInformation
        List<EmployeeInformation> allEmployeeInfos = employees.stream()
                .map(emp -> {
                    EmployeeInformation info = new EmployeeInformation();
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
                    info.setRole("Manager".equalsIgnoreCase(emp.getCategory()) ? "MANAGER" : "EMPLOYEE");
                    info.setCategory(emp.getCategory());
                    info.setReportingManager(emp.getReportingManager());
                    info.setDepartment(departmentCache.get(emp.getDepartment()));
                    return info;
                })
                .collect(Collectors.toList());

        // Save into repository
        if (!allEmployeeInfos.isEmpty()) {
            employeeInformationRepository.saveAll(allEmployeeInfos);
        }

        // Count managers and employees
        long managerCount = allEmployeeInfos.stream()
                .filter(e -> "MANAGER".equalsIgnoreCase(e.getRole()))
                .count();

        long employeeCount = allEmployeeInfos.size() - managerCount;

        return List.of(
                "Managers Saved: " + managerCount,
                "Employees Saved: " + employeeCount
        );
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
        Optional<EmployeeInformation>  employeeById = employeeInformationRepository.findById(employeeId);
        if(employeeById.isPresent()){
            EmployeeInformation employeeInformation = employeeById.get();
            employeeInformationRepository.delete(employeeInformation);
            response.put("message","Employee deleted with ID  " + employeeId );
            return response;
        }
        else {
            response.put("message","Employee Not found ");
            return response;

        }



    }


    private HrResponseDto mapToHrResponseDto(HR hr){
        return modelMapper.map(hr, HrResponseDto.class);
    }

    private CountryResponseDto mapToCountryResponseDto(Country country){
        return modelMapper.map(country, CountryResponseDto.class);
    }

    private StateResponseDto mapToStateResponseDto(State state){
        return modelMapper.map(state, StateResponseDto.class);
    }

    private LocationResponseDto mapToLocationResponseDto(Location location){
        return modelMapper.map(location, LocationResponseDto.class);
    }

    private DepartmentResponseDto mapToDepartmentResponseDto(Department department){
        return modelMapper.map(department, DepartmentResponseDto.class);
    }

    private ProjectResponseDto mapToProjectResponseDto(Project project){
        return modelMapper.map(project,ProjectResponseDto.class);


    }

    private ManagerResponseDto mapToManagerResponseDto(Manager manager){
        return modelMapper.map(manager, ManagerResponseDto.class);
    }

}
