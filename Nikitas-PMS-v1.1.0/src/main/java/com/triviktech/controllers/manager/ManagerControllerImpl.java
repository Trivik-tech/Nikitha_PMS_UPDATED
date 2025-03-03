package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.services.manager.ManagerService;
import com.triviktech.utilities.validation.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ManagerControllerImpl implements ManagerController{

    private final ManagerService managerService;

    public ManagerControllerImpl(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public ResponseEntity<?> registerManager(ManagerRequestDto managerRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(managerService.registerManager(managerRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ManagerResponseDto>> listOfManagers() {
        return ResponseEntity.ok(managerService.listOfManager());
    }

    @Override
    public ResponseEntity<ManagerResponseDto> getManager(String managerId) {
        ManagerResponseDto managerById = managerService.findManagerById(managerId);
        return ResponseEntity.ok(managerById);
    }

    @Override
    public  ResponseEntity<ManagerResponseDto> profile(@AuthenticationPrincipal UserDetails manager) {
        return ResponseEntity.ok(managerService.findManagerById(manager.getUsername()));
    }

    @Override
    public ResponseEntity<List<EmployeeInformationResponseDto>> listOfEmployeesForManager(String managerId) {
        return ResponseEntity.ok(managerService.listOfEmployeesForManager(managerId));
    }
}
