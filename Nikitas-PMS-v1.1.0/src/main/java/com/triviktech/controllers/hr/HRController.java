package com.triviktech.controllers.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatusCountDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * HRController is the REST API interface for handling all HR-related operations
 * in the PMS (Performance Management System) application.
 *
 * <p>
 * Responsibilities include:
 * </p>
 * <ul>
 * <li>Registering HR users and employees.</li>
 * <li>Fetching and updating employee data, including search and department-wise
 * statistics.</li>
 * <li>Handling KRA/KPI and PMS (Performance Management System) workflows.</li>
 * <li>Generating reports and exporting employee data in PDF format.</li>
 * <li>Sending notifications to employees and managers.</li>
 * <li>Retrieving HR profile information and PMS status summaries.</li>
 * </ul>
 *
 * <p>
 * All endpoints are CORS-enabled for "http://localhost:3000".
 * </p>
 */
@RequestMapping("/api/v1/pms/hr")
@CrossOrigin(origins = "http://localhost:3000")
public interface HRController {

    @PostMapping("/register-hr")
    ResponseEntity<?> registerHr(@RequestBody HrRequestDto hrRequestDto);

    @GetMapping("/{hrId}")
    ResponseEntity<HrResponseDto> getHrById(@PathVariable String hrId);

    @PostMapping("/upload")
    ResponseEntity<List<Object>> uploadEmployeesData(@RequestParam("file") MultipartFile file) throws IOException;

    @GetMapping("/all-employees")
    ResponseEntity<List<EmployeeInfo>> allEmployees();

    @GetMapping("/get-employee/{employeeId}")
    ResponseEntity<EmployeeInfo> getEmployee(@PathVariable String employeeId);

    @GetMapping("/total-employees")
    ResponseEntity<Map<String, Integer>> totalEmployees();

    @GetMapping("/all-employees/{search}")
    ResponseEntity<List<EmployeeInfo>> searchEmployee(@PathVariable String search);

    @DeleteMapping("/delete-employee/{employeeId}")
    ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String employeeId);

    @PutMapping("/update-employee/{empId}")
    ResponseEntity<Map<String, String>> updateEmployee(@PathVariable String empId, @RequestBody Employee employee);

    @GetMapping("/employee-list")
    ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved();

    @GetMapping("/get-departments")
    ResponseEntity<Map<String, Object>> getDepartment();

    @GetMapping("/employee-count-by-department")
    ResponseEntity<Map<String, List<Long>>> employeeCount();

    @GetMapping("/keyMatrix")
    ResponseEntity<Map<String, Integer>> keyMatrix();

    @PatchMapping("/pms-initiated/{employeeId}")
    ResponseEntity<Map<String, String>> pmsInitiated(@PathVariable String employeeId,
            @RequestBody Map<String, Boolean> pms);

    @GetMapping("/employee-with-pms-initiated")
    ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees();

    @GetMapping("/completed")
    ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR();

    @PostMapping("/register-employee")
    ResponseEntity<Map<String, String>> registerEmployee(@Valid @RequestBody Employee employee,
            BindingResult bindingResult);

    @GetMapping("/pending")
    ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR();

    @GetMapping("/percentage")
    ResponseEntity<PmsPercentageDto> getPmsPercentageForHR();

    @PostMapping("/notify/employee-and-manager/{employeeId}")
    ResponseEntity<Map<String, String>> notifyEmployeeAndManager(@PathVariable String employeeId);

    @GetMapping("/profile")
    ResponseEntity<Map<String, HrResponseDto>> profile(@AuthenticationPrincipal UserDetails hr);

    /**
     * Retrieves PMS status counts for HR.
     */
    @GetMapping("/status-count")
    ResponseEntity<PmsStatusCountDto> getPmsStatusCountForHR();

    @GetMapping("/generate-report/{id}")
    ResponseEntity<Map<String, String>> generateEmployeeInfoReport(@PathVariable String id);

    @GetMapping("/generate-employee-list")
    ResponseEntity<Map<String, String>> generateEmployeeList();

    @GetMapping("/get-completed-pending-department")
    ResponseEntity<Map<String, Map<String, Integer>>> getCompletedPendingByDepartments();

    @GetMapping("/export-pdf/{employeeId}")
    ResponseEntity<InputStreamResource> exportPmsPdf(@PathVariable String employeeId);

    @PostMapping("/upload-kra-kpi")
    ResponseEntity<Map<String, List<XlsxSupport.KRA>>> uploadKraKpi(@RequestParam("file") MultipartFile file)
            throws Exception;

    @PostMapping("/exit-employee/{empId}/{lastWorkingDay}")
    ResponseEntity<String> processEmployeeExit(
            @PathVariable String empId,
            @PathVariable("lastWorkingDay") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate lastWorkingDay);

    void notifyAllEmployeesAndManagers();

    @GetMapping("/monthly")
    public Map<String, Map<String, Integer>> getMonthlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter,
            @RequestParam int month);

    @GetMapping("/quarterly")
    public Map<String, Map<String, Integer>> getQuarterlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter);

    @GetMapping("/yearly")
    public Map<String, Map<String, Integer>> getYearlyDepartmentReport(
            @RequestParam int year);

    @GetMapping("consolidated-report")
    public ResponseEntity<InputStreamResource> downloadAllEmployeesReport();
}
