package com.triviktech.controllers.hr;

import com.triviktech.entities.employee.ExitEmployee;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.*;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.utilities.xlsxsupport.XlsxSupport;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/pms/hr")
public interface HRController {

    @PostMapping("/register")
    ResponseEntity<?> registerHr(@RequestBody HrRequestDto hrRequestDto);

    @GetMapping("/{hrId}")
    ResponseEntity<HrResponseDto> getHrById(@PathVariable String hrId);

    @PostMapping("/upload-employees")
    ResponseEntity<List<Object>> uploadEmployeesData(@RequestParam("file") MultipartFile file) throws IOException;

    @GetMapping("/employees")
    ResponseEntity<List<EmployeeInfo>> allEmployees();

    @GetMapping("/employee/{employeeId}")
    ResponseEntity<EmployeeInfo> getEmployee(@PathVariable String employeeId);

    @GetMapping("/total-employees")
    ResponseEntity<Map<String, Integer>> totalEmployees();

    @GetMapping("/search")
    ResponseEntity<List<EmployeeInfo>> searchEmployee(@RequestParam String search);

    @DeleteMapping("/employee/{employeeId}")
    ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String employeeId);

    @PutMapping("/employee/{empId}")
    ResponseEntity<Map<String, String>> updateEmployee(@PathVariable String empId, @RequestBody Employee employee);

    @GetMapping("/kra-kpi/approved")
    ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved();

    @GetMapping("/departments")
    ResponseEntity<Map<String, Object>> getDepartment();

    @GetMapping("/employee-count")
    ResponseEntity<Map<String, List<Long>>> employeeCount();

    @GetMapping("/key-matrix")
    ResponseEntity<Map<String, Integer>> keyMatrix();

    @PostMapping("/pms/initiate/{employeeId}")
    ResponseEntity<Map<String, String>> pmsInitiated(@PathVariable String employeeId, @RequestBody Map<String, Boolean> pms);

    @GetMapping("/pms/initiated-employees")
    ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees();

    @GetMapping("/pms/completed")
    ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR();

    @PostMapping("/employee/register")
    ResponseEntity<Map<String, String>> registerEmployee(@RequestBody Employee employee, BindingResult bindingResult);

    @GetMapping("/pms/pending")
    ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR();

    @GetMapping("/pms/percentage")
    ResponseEntity<PmsPercentageDto> getPmsPercentageForHR();

    @PostMapping("/notify/{employeeId}")
    ResponseEntity<Map<String, String>> notifyEmployeeAndManager(@PathVariable String employeeId);

    @GetMapping("/profile")
    ResponseEntity<Map<String, HrResponseDto>> profile(@RequestBody UserDetails hr);

    @GetMapping("/pms/status-count")
    ResponseEntity<PmsStatusCountDto> getPmsStatusCountForHR();

    @GetMapping("/report/employee/{id}")
    ResponseEntity<Map<String, String>> generateEmployeeInfoReport(@PathVariable String id);

    @GetMapping("/report/employees")
    ResponseEntity<Map<String, String>> generateEmployeeList();

    @GetMapping("/department/completed-pending")
    ResponseEntity<Map<String, Map<String, Integer>>> getCompletedPendingByDepartments();

    @GetMapping("/export/{employeeId}")
    ResponseEntity<InputStreamResource> exportPmsPdf(@PathVariable String employeeId);

    @PostMapping("/kra-kpi/upload")
    ResponseEntity<Map<String, List<XlsxSupport.KRA>>> uploadKraKpi(@RequestParam("file") MultipartFile file);

    @PostMapping("/exit-employee/{empId}/{lastWorkingDay}")
    ResponseEntity<String> processEmployeeExit(
            @PathVariable String empId,
            @PathVariable("lastWorkingDay") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate lastWorkingDay);

    @GetMapping("/exit-employees")
    ResponseEntity<List<ExitEmployee>> getAllExitEmployees();

    @GetMapping("/department/monthly")
    ResponseEntity<Map<String, Map<String, Integer>>> getMonthlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter,
            @RequestParam int month);

    @GetMapping("/department/quarterly")
    ResponseEntity<Map<String, Map<String, Integer>>> getQuarterlyDepartmentReport(
            @RequestParam int year,
            @RequestParam int quarter);

    @GetMapping("/department/yearly")
    ResponseEntity<Map<String, Map<String, Integer>>> getYearlyDepartmentReport(@RequestParam int year);

    @GetMapping("/report/all-employees")
    ResponseEntity<InputStreamResource> downloadAllEmployeesReport();

//     @PostMapping("/notify-all")
//     ResponseEntity<Map<String, String>> notifyAllEmployeesAndManagers();
}
