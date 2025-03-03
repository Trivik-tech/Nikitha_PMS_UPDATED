package com.triviktech.services.department;

import com.triviktech.payloads.request.department.DepartmentRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;

import java.util.Set;

public interface DepartmentService {

    DepartmentResponseDto registerDepartment(DepartmentRequestDto departmentRequestDto);
    Set<DepartmentResponseDto> listOfDepartments();

    DepartmentResponseDto updateDepartment(DepartmentRequestDto departmentRequestDto,long departmentId);

    DepartmentResponseDto getDepartment(long departmentId);
}
