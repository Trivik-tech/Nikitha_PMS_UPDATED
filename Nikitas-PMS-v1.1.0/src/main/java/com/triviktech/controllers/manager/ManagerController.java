package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/pms/manager")
@CrossOrigin(origins = "http://localhost:3000")
public interface ManagerController {

    @PostMapping("/register-manager")
    ResponseEntity<?> registerManager(@Valid @RequestBody ManagerRequestDto managerRequestDto, BindingResult bindingResult);

    @GetMapping("/manager-list")
    ResponseEntity<List<ManagerResponseDto>> listOfManagers();

    @GetMapping("/{managerId}")
    ResponseEntity<ManagerResponseDto> getManager(@PathVariable String managerId);

    @GetMapping("/profile")
    ResponseEntity<ManagerResponseDto> profile(@AuthenticationPrincipal UserDetails manager);

    @GetMapping("/employee-list/{managerId}")
    ResponseEntity<List<EmployeeInformationResponseDto>> listOfEmployeesForManager(@PathVariable String managerId);
}
