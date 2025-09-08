package com.triviktech.controllers.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.request.kra.KraRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * HRController is the REST API interface for handling all HR-related operations
 * in the PMS (Performance Management System) application.
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Registering HR users and employees.</li>
 *   <li>Fetching and updating employee data, including search and department-wise statistics.</li>
 *   <li>Handling KRA/KPI and PMS (Performance Management System) workflows.</li>
 *   <li>Generating reports and exporting employee data in PDF format.</li>
 *   <li>Sending notifications to employees and managers.</li>
 *   <li>Retrieving HR profile information and PMS status summaries.</li>
 * </ul>
 *
 * <p>All endpoints are CORS-enabled for "http://localhost:3000".</p>
 */

@RequestMapping("/api/v1/pms/hr")
@CrossOrigin(origins = "http://localhost:3000")
public interface HRController {

        /**
         * Registers a new HR user.
         *
         * @param hrRequestDto DTO containing HR registration details.
         * @return ResponseEntity with status and message.
         */
        @PostMapping("/register-hr")
        ResponseEntity<?> registerHr(@RequestBody HrRequestDto hrRequestDto);

        /**
         * Retrieves an HR user's details by their HR ID.
         *
         * @param hrId HR ID.
         * @return ResponseEntity containing HrResponseDto.
         */
        @GetMapping("/{hrId}")
        ResponseEntity<HrResponseDto> getHrById(@PathVariable String hrId);

        /**
         * Uploads employee data from a file.
         *
         * @param file Multipart file containing employee data (XLSX/CSV).
         * @return ResponseEntity containing a list of objects representing upload results.
         * @throws IOException if file processing fails.
         */
        @PostMapping("/upload")
        ResponseEntity<List<Object>> uploadEmployeesData(@RequestParam("file") MultipartFile file) throws IOException;

        /**
         * Retrieves all employees.
         *
         * @return ResponseEntity containing a list of EmployeeInfo objects.
         */
        @GetMapping("/all-employees")
        ResponseEntity<List<EmployeeInfo>> allEmployees();

        /**
         * Retrieves information of a specific employee by employee ID.
         *
         * @param employeeId Employee ID.
         * @return ResponseEntity containing EmployeeInfo.
         */
        @GetMapping("/get-employee/{employeeId}")
        ResponseEntity<EmployeeInfo> getEmployee(@PathVariable String employeeId);

        /**
         * Retrieves total number of employees.
         *
         * @return ResponseEntity containing a map with total count.
         */
        @GetMapping("/total-employees")
        ResponseEntity<Map<String, Integer>> totalEmployees();

        /**
         * Searches employees based on a search string.
         *
         * @param search Search string.
         * @return ResponseEntity containing a list of EmployeeInfo.
         */
        @GetMapping("/all-employees/{search}")
        ResponseEntity<List<EmployeeInfo>> searchEmployee(@PathVariable String search);

        /**
         * Deletes an employee by employee ID.
         *
         * @param employeeId Employee ID to delete.
         * @return ResponseEntity containing status message.
         */
        @DeleteMapping("/delete-employee/{employeeId}")
        ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String employeeId);

        /**
         * Updates an employee's information.
         *
         * @param empId Employee ID.
         * @param employee Updated employee data.
         * @return ResponseEntity containing status message.
         */
        @PutMapping("/update-employee/{empId}")
        ResponseEntity<Map<String, String>> updateEmployee(@PathVariable String empId, @RequestBody Employee employee);

        /**
         * Retrieves employees with approved KRA/KPI.
         *
         * @return ResponseEntity containing a list of EmployeeInfo.
         */
        @GetMapping("/employee-list")
        ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved();

        /**
         * Retrieves all departments.
         *
         * @return ResponseEntity containing department details.
         */
        @GetMapping("/get-departments")
        ResponseEntity<Map<String, Object>> getDepartment();

        /**
         * Retrieves employee count grouped by department.
         *
         * @return ResponseEntity containing department-wise counts.
         */
        @GetMapping("/employee-count-by-department")
        ResponseEntity<Map<String, List<Long>>> employeeCount();

        /**
         * Retrieves key matrix metrics for HR overview.
         *
         * @return ResponseEntity containing key metrics.
         */
        @GetMapping("/keyMatrix")
        ResponseEntity<Map<String, Integer>> keyMatrix();

        /**
         * Marks PMS as initiated for an employee.
         *
         * @param employeeId Employee ID.
         * @param pms Map containing PMS initiation status.
         * @return ResponseEntity containing status message.
         */
        @PatchMapping("/pms-initiated/{employeeId}")
        ResponseEntity<Map<String, String>> pmsInitiated(@PathVariable String employeeId,
                                                         @RequestBody Map<String, Boolean> pms);

        /**
         * Retrieves employees with PMS initiated.
         *
         * @return ResponseEntity containing list of EmployeeInfo.
         */
        @GetMapping("/employee-with-pms-initiated")
        ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees();

        /**
         * Retrieves employees with completed PMS.
         *
         * @return ResponseEntity containing list of EmployeeWithPmsStatus.
         */
        @GetMapping("/completed")
        ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR();

        /**
         * Registers a new employee.
         *
         * @param employee Employee DTO.
         * @param bindingResult Validation result.
         * @return ResponseEntity containing status message.
         */
        @PostMapping("/register-employee")
        ResponseEntity<Map<String, String>> registerEmployee(@Valid @RequestBody Employee employee,
                                                             BindingResult bindingResult);

        /**
         * Retrieves employees with pending PMS.
         *
         * @return ResponseEntity containing list of EmployeeWithPmsStatus.
         */
        @GetMapping("/pending")
        ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR();

        /**
         * Retrieves PMS completion percentage for HR overview.
         *
         * @return ResponseEntity containing PmsPercentageDto.
         */
        @GetMapping("/percentage")
        ResponseEntity<PmsPercentageDto> getPmsPercentageForHR();

        /**
         * Sends notifications to both employee and manager.
         *
         * @param employeeId Employee ID.
         * @return ResponseEntity containing status message.
         */
        @PostMapping("/notify/employee-and-manager/{employeeId}")
        ResponseEntity<Map<String, String>> notifyEmployeeAndManager(@PathVariable String employeeId);

        /**
         * Retrieves profile of the logged-in HR.
         *
         * @param hr HR user details from authentication principal.
         * @return ResponseEntity containing HR profile information.
         */
        @GetMapping("/profile")
        ResponseEntity<Map<String, HrResponseDto>> profile(@AuthenticationPrincipal UserDetails hr);

        /**
         * Retrieves PMS status counts for HR.
         *
         * @return ResponseEntity containing PmsStatusCountDto.
         */
        @GetMapping("/status-count")
        ResponseEntity<PmsStatusCountDto> getPmsStatusCountForHR();

        /**
         * Generates detailed report for a specific employee.
         *
         * @param id Employee ID.
         * @return ResponseEntity containing report status message.
         */
        @GetMapping("/generate-report/{id}")
        ResponseEntity<Map<String,String>> generateEmployeeInfoReport(@PathVariable String id);

        /**
         * Generates a complete employee list report.
         *
         * @return ResponseEntity containing report status message.
         */
        @GetMapping("/generate-employee-list")
        ResponseEntity<Map<String,String>> generateEmployeeList();

        /**
         * Retrieves completed and pending PMS counts grouped by department.
         *
         * @return ResponseEntity containing department-wise completed/pending counts.
         */
        @GetMapping("/get-completed-pending-department")
        ResponseEntity<Map<String, Map<String, Integer>>> getCompletedPendingByDepartments();

        /**
         * Exports PMS report for a specific employee as a PDF.
         *
         * @param employeeId Employee ID.
         * @return ResponseEntity containing InputStreamResource for PDF download.
         */
        @GetMapping("/export-pdf/{employeeId}")
        ResponseEntity<InputStreamResource> exportPmsPdf(@PathVariable String employeeId);


        @PostMapping("/upload-kra-kpi")
        ResponseEntity<Map<String,List<XlsxSupport.KRA>>> uploadKraKpi(@RequestParam("file") MultipartFile file) throws Exception;
}
