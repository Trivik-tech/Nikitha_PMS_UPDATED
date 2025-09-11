package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatuscountDto;
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
        ResponseEntity<?> registerManager(@Valid @RequestBody ManagerRequestDto managerRequestDto,
                        BindingResult bindingResult);

        @GetMapping("/manager-list")
        ResponseEntity<List<ManagerResponseDto>> listOfManagers();

        @GetMapping("/{managerId}")
        ResponseEntity<ManagerResponseDto> getManager(@PathVariable String managerId);

        @GetMapping("/profile")
        ResponseEntity<ManagerResponseDto> profile(@AuthenticationPrincipal UserDetails manager);

        @GetMapping("/employee-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfEmployeesForManager(@PathVariable String managerId);

        @GetMapping("/pending-pms-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSPendingEmployees(@PathVariable String managerId);

        @GetMapping("/completed-pms-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSCompletedEmployees(@PathVariable String managerId);

        @PatchMapping("/manager-review/{managerId}/{employeeId}")
        ResponseEntity<Map<String, String>> managerReview(@PathVariable String managerId,
                        @PathVariable String employeeId,
                        @RequestBody KraKpiRequestDto data);

        @GetMapping("/kra-kpi/{managerId}/{employeeId}")
        ResponseEntity<KraKpiResponseDto> getKraKpis(@PathVariable String managerId,
                        @PathVariable String employeeId);

        @PatchMapping("/approve-krakpi/{employeeId}/{managerId}")
        ResponseEntity<Map<String, String>> approveKraKpi(@PathVariable String employeeId,
                        @PathVariable String managerId,
                        @RequestBody KraKpiRequestDto kraKpiRequestDto);

        // @GetMapping("/manager-team/{reportingManager}")
        // ResponseEntity<List<EmployeeInfo>> getManagerTeam(@PathVariable String
        // reportingManager);

        @GetMapping("/completed/{reportingManager}")
        ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForManager(@PathVariable String reportingManager);

        @GetMapping("/pending/{reportingManager}")
        ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForManager(@PathVariable String reportingManager);

        @GetMapping("/percentage/{reportingManager}")
        ResponseEntity<PmsPercentageDto> getPmsPercentageForManager(@PathVariable String reportingManager);

        @PostMapping("/notify/employee/{employeeId}")
        ResponseEntity<Map<String, String>> notifyEmployee(@PathVariable String employeeId);

        @GetMapping("/get-team-size/{managerId}")
        ResponseEntity<Map<String, Integer>> getTeamSize(@PathVariable String managerId);

        @GetMapping("/pms/status-count/{managerId}")
        ResponseEntity<PmsStatuscountDto> getPmsCountsForManager(@PathVariable String managerId);

        // public void autoNotifyEmployees();
}
