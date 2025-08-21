package com.triviktech.controllers.employee;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * EmployeeController is a REST API interface that defines endpoints related to
 * employee operations in the PMS (Performance Management System).
 *
 * Responsibilities:
 * 1. Fetch and manage KRA/KPI data for employees.
 * 2. Allow employees to register KRA/KPI entries and perform self-reviews.
 * 3. Provide access to employee profile information.
 * 4. Check for existence of KRA/KPI entries.
 *
 * All endpoints are accessible under the base path: /api/v1/pms/employee
 * Cross-origin requests are allowed from http://localhost:3000.
 */
@RequestMapping("/api/v1/pms/employee")
@CrossOrigin(origins = "http://localhost:3000")
public interface EmployeeController {

    /**
     * Fetches the KRA/KPI information for a specific employee.
     *
     * @param employeeId The ID of the employee whose KRA/KPI data is requested.
     * @return KraKpiResponseDto containing the KRA/KPI details for the employee.
     */
    @GetMapping("/kra-kpi-list/{employeeId}")
    ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(@PathVariable String employeeId);

    /**
     * Registers KRA/KPI entries for an employee.
     *
     * @param kraKpiRequestDto The KRA/KPI details to register.
     * @return A map containing success or error messages related to the registration.
     */
    @PostMapping("/register-kra-kpi")
    ResponseEntity<Map<String,String>> kraKpiRegistrationForEmployee(@RequestBody KraKpiRequestDto kraKpiRequestDto);

    /**
     * Fetches the profile information of the authenticated employee.
     *
     * @param employee The authenticated employee's details.
     * @return EmployeeInfo object containing profile details of the employee.
     */
    @GetMapping("/profile")
    ResponseEntity<EmployeeInfo> profile(@AuthenticationPrincipal UserDetails employee);

    /**
     * Allows an employee to submit a self-review for their KRA/KPI entries.
     *
     * @param kraKpiRequestDto The KRA/KPI self-review data.
     * @param employeeId The ID of the employee performing the self-review.
     * @return A map containing success or error messages related to the self-review.
     */
    @PutMapping("/self-review/{employeeId}")
    ResponseEntity<Map<String,String >> selfReview(@RequestBody KraKpiRequestDto kraKpiRequestDto,@PathVariable String employeeId);

    /**
     * Checks if KRA/KPI entries already exist for a specific employee.
     *
     * @param employeeId The ID of the employee to check.
     * @return A map with a boolean value indicating whether KRA/KPI already exists.
     */
    @GetMapping("/check-kra-kpi/{employeeId}")
    ResponseEntity<Map<String,Boolean>> checkKraKpiAlreadyExists(@PathVariable String employeeId);

    /**
     * Fetches the list of all KRA/KPI entries for a specific employee, organized by category.
     *
     * @param employeeId The ID of the employee whose KRA/KPI entries are requested.
     * @return A map where the key is a category and the value is a list of KraKpiResponseDto objects.
     */
    @GetMapping("/krakpi-list/{employeeId}")
    ResponseEntity<Map<String, List<KraKpiResponseDto>>> listOfKraKpi(@PathVariable String employeeId);
}
