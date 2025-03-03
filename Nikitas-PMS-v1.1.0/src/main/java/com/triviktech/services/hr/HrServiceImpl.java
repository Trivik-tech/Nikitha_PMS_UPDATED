package com.triviktech.services.hr;

import com.triviktech.entities.address.Country;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.entities.department.Department;
import com.triviktech.entities.hr.HR;
import com.triviktech.entities.manager.Manager;
import com.triviktech.entities.project.Project;
import com.triviktech.exception.address.StateNotFoundException;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.exception.hr.HRNotFoundException;
import com.triviktech.exception.manager.ManagerNotFoundException;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.repositories.hr.HRRepository;
import com.triviktech.repositories.manager.ManagerRepository;
import com.triviktech.repositories.project.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
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


    public HrServiceImpl(HRRepository hrRepository, StateRepository stateRepository, LocationRepository locationRepository, DepartmentRepository departmentRepository, ProjectRepository projectRepository, ManagerRepository managerRepository, ModelMapper modelMapper) {
        this.hrRepository = hrRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.managerRepository = managerRepository;
        this.modelMapper = modelMapper;
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
