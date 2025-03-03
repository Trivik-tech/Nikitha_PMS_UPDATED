package com.triviktech.controllers.department;

import com.triviktech.payloads.request.department.DepartmentRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/v1/pms/department")
public interface DepartmentController {

    @PostMapping("/register-department")
    ResponseEntity<?> registerDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestDto, BindingResult bindingResult);

    @GetMapping("/department-list")
    ResponseEntity<Set<DepartmentResponseDto>> departmentList();

    @PutMapping("/update-department/{departmentId}")
    ResponseEntity<?> updateDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestD,@PathVariable long departmentId ,BindingResult bindingResult);

    @GetMapping("/{departmentId}")
    ResponseEntity<DepartmentResponseDto> getDepartment(@PathVariable long departmentId);

}
