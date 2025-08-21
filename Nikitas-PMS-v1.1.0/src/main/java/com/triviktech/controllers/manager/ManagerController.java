package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
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

/**
 * ManagerController defines the REST API endpoints for manager-related operations
 * in the Performance Management System (PMS).
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Manager registration and profile management.</li>
 *   <li>Fetching employees assigned to a manager with PMS status.</li>
 *   <li>Tracking PMS completion, pending reviews, and progress percentage.</li>
 *   <li>Manager reviews and approvals for employee KRA/KPI.</li>
 *   <li>Notifications to employees regarding PMS activities.</li>
 *   <li>Providing team size and PMS statistics for dashboards.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Base URL: <code>/api/v1/pms/manager</code><br>
 * Allows cross-origin requests from <code>http://localhost:3000</code>.
 * </p>
 */
@RequestMapping("/api/v1/pms/manager")
@CrossOrigin(origins = "http://localhost:3000")
public interface ManagerController {

        /**
         * Registers a new manager in the PMS system.
         *
         * @param managerRequestDto Manager details from the request.
         * @param bindingResult Validation errors if present.
         * @return ResponseEntity with success or error message.
         */
        @PostMapping("/register-manager")
        ResponseEntity<?> registerManager(@Valid @RequestBody ManagerRequestDto managerRequestDto,
                                          BindingResult bindingResult);

        /**
         * Retrieves the list of all registered managers.
         *
         * @return ResponseEntity containing a list of {@link ManagerResponseDto}.
         */
        @GetMapping("/manager-list")
        ResponseEntity<List<ManagerResponseDto>> listOfManagers();

        /**
         * Fetches details of a specific manager by their ID.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing {@link ManagerResponseDto}.
         */
        @GetMapping("/{managerId}")
        ResponseEntity<ManagerResponseDto> getManager(@PathVariable String managerId);

        /**
         * Retrieves the profile of the currently authenticated manager.
         *
         * @param manager Authenticated manager user details.
         * @return ResponseEntity containing {@link ManagerResponseDto}.
         */
        @GetMapping("/profile")
        ResponseEntity<ManagerResponseDto> profile(@AuthenticationPrincipal UserDetails manager);

        /**
         * Retrieves the list of employees under a given manager with their PMS status.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing a list of {@link EmployeeWithPmsStatus}.
         */
        @GetMapping("/employee-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfEmployeesForManager(@PathVariable String managerId);

        /**
         * Retrieves the list of employees who have pending PMS reviews under a manager.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing a list of {@link EmployeeWithPmsStatus}.
         */
        @GetMapping("/pending-pms-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSPendingEmployees(@PathVariable String managerId);

        /**
         * Retrieves the list of employees who have completed PMS under a manager.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing a list of {@link EmployeeWithPmsStatus}.
         */
        @GetMapping("/completed-pms-list/{managerId}")
        ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSCompletedEmployees(@PathVariable String managerId);

        /**
         * Allows a manager to provide PMS review for a specific employee.
         *
         * @param managerId Manager ID.
         * @param employeeId Employee ID.
         * @param data KRA/KPI review data.
         * @return ResponseEntity containing success message in a Map.
         */
        @PatchMapping("/manager-review/{managerId}/{employeeId}")
        ResponseEntity<Map<String, String>> managerReview(@PathVariable String managerId,
                                                          @PathVariable String employeeId,
                                                          @RequestBody KraKpiRequestDto data);

        /**
         * Retrieves the KRA/KPI details of a specific employee under a manager.
         *
         * @param managerId Manager ID.
         * @param employeeId Employee ID.
         * @return ResponseEntity containing {@link KraKpiResponseDto}.
         */
        @GetMapping("/kra-kpi/{managerId}/{employeeId}")
        ResponseEntity<KraKpiResponseDto> getKraKpis(@PathVariable String managerId,
                                                     @PathVariable String employeeId);

        /**
         * Allows a manager to approve an employee’s KRA/KPI.
         *
         * @param employeeId Employee ID.
         * @param managerId Manager ID.
         * @param kraKpiRequestDto Approval details.
         * @return ResponseEntity containing success message in a Map.
         */
        @PatchMapping("/approve-krakpi/{employeeId}/{managerId}")
        ResponseEntity<Map<String, String>> approveKraKpi(@PathVariable String employeeId,
                                                          @PathVariable String managerId,
                                                          @RequestBody KraKpiRequestDto kraKpiRequestDto);

        /**
         * Retrieves the list of employees who have completed PMS for a reporting manager.
         *
         * @param reportingManager Reporting manager ID.
         * @return ResponseEntity containing a list of {@link EmployeeWithPmsStatus}.
         */
        @GetMapping("/completed/{reportingManager}")
        ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForManager(@PathVariable String reportingManager);

        /**
         * Retrieves the list of employees who have pending PMS for a reporting manager.
         *
         * @param reportingManager Reporting manager ID.
         * @return ResponseEntity containing a list of {@link EmployeeWithPmsStatus}.
         */
        @GetMapping("/pending/{reportingManager}")
        ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForManager(@PathVariable String reportingManager);

        /**
         * Retrieves the PMS completion percentage for a manager’s team.
         *
         * @param reportingManager Reporting manager ID.
         * @return ResponseEntity containing {@link PmsPercentageDto}.
         */
        @GetMapping("/percentage/{reportingManager}")
        ResponseEntity<PmsPercentageDto> getPmsPercentageForManager(@PathVariable String reportingManager);

        /**
         * Sends a notification to a specific employee regarding PMS updates.
         *
         * @param employeeId Employee ID.
         * @return ResponseEntity containing success message in a Map.
         */
        @PostMapping("/notify/employee/{employeeId}")
        ResponseEntity<Map<String, String>> notifyEmployee(@PathVariable String employeeId);

        /**
         * Retrieves the team size for a specific manager.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing team size in a Map.
         */
        @GetMapping("/get-team-size/{managerId}")
        ResponseEntity<Map<String, Integer>> getTeamSize(@PathVariable String managerId);

        /**
         * Retrieves PMS status counts (completed, pending, etc.) for a manager’s team.
         *
         * @param managerId Manager ID.
         * @return ResponseEntity containing {@link PmsStatusCountDto}.
         */
        @GetMapping("/pms/status-count/{managerId}")
        ResponseEntity<PmsStatusCountDto> getPmsCountsForManager(@PathVariable String managerId);
}
