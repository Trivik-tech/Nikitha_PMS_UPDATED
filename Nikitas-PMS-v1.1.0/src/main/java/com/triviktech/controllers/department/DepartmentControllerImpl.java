package com.triviktech.controllers.department;

import com.triviktech.payloads.request.department.DepartmentRequestDto;
import com.triviktech.payloads.response.department.DepartmentResponseDto;
import com.triviktech.services.department.DepartmentService;
import com.triviktech.utilities.validation.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

@RestController
public class DepartmentControllerImpl implements DepartmentController{

  private final DepartmentService departmentService;

    public DepartmentControllerImpl(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @Override
    public ResponseEntity<?> registerDepartment(DepartmentRequestDto departmentRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(departmentService.registerDepartment(departmentRequestDto),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Set<DepartmentResponseDto>> departmentList() {
        return ResponseEntity.ok(departmentService.listOfDepartments());
    }

    @Override
    public ResponseEntity<?> updateDepartment(DepartmentRequestDto departmentRequestD, long departmentId, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(departmentService.updateDepartment(departmentRequestD,departmentId));
    }

    @Override
    public ResponseEntity<DepartmentResponseDto> getDepartment(long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartment(departmentId));
    }
}
