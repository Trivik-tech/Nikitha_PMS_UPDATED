package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/employee-list/{reportingManager}")
    ResponseEntity<List<EmployeeWithPmsStatus>> listOfEmployeesForManager(@PathVariable String reportingManager);

    @GetMapping("/pending-pms-list/{managerId}")
    ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSPendingEmployees(@PathVariable String managerId);

    @GetMapping("/completed-pms-list/{managerId}")
    ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSCompletedEmployees(@PathVariable String managerId);

    @PatchMapping("/manager-review/{managerName}/{employeeId}")
    ResponseEntity<Map<String,String>> managerReview(@PathVariable String  managerName,@PathVariable String employeeId, @RequestBody KraKpiRequestDto data);


    @GetMapping("/kra-kpi/{managerName}/{employeeId}")
    ResponseEntity<KraKpiResponseDto> getKraKpis(@PathVariable String managerName,  @PathVariable String employeeId);

    @GetMapping("/manager-team/{reportingManager}")
    ResponseEntity<List<EmployeeInfo> > getManagerTeam(@PathVariable String reportingManager);

    @PatchMapping("/approve-krakpi/{employeeId}/{reportingManager}")
    ResponseEntity<Map<String, String>> approveKraKpi(@PathVariable String employeeId, @PathVariable String reportingManager, @RequestBody Map<String, Boolean> approve);


}
