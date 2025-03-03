package com.triviktech.services.department;

import com.triviktech.entities.department.Department;
import com.triviktech.exception.department.DepartmentNotFoundException;
import com.triviktech.payloads.request.department.DepartmentRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.repositories.department.DepartmentRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EntityDtoConversion entityDtoConversion;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EntityDtoConversion entityDtoConversion) {
        this.departmentRepository = departmentRepository;
        this.entityDtoConversion = entityDtoConversion;
    }

    @Override
    public DepartmentResponseDto registerDepartment(DepartmentRequestDto departmentRequestDto) {
        Department department = entityDtoConversion.dtoToEntityConversion(departmentRequestDto, Department.class);
        Department saved = departmentRepository.save(department);
        DepartmentResponseDto departmentResponseDto = entityDtoConversion.entityToDtoConversion(saved, DepartmentResponseDto.class);
        departmentResponseDto.setStatus(HttpStatus.CREATED);
        return departmentResponseDto;
    }

    @Override
    public Set<DepartmentResponseDto> listOfDepartments() {
        List<Department> all = departmentRepository.findAll();
        return all.stream()
                .map(department -> entityDtoConversion.entityToDtoConversion(department, DepartmentResponseDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public DepartmentResponseDto updateDepartment(DepartmentRequestDto departmentRequestDto, long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        department.setName(departmentRequestDto.getName());
        Department saved = departmentRepository.save(department);
        return entityDtoConversion.entityToDtoConversion(saved, DepartmentResponseDto.class);
    }

    @Override
    public DepartmentResponseDto getDepartment(long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        return entityDtoConversion.entityToDtoConversion(department, DepartmentResponseDto.class);
    }
}
