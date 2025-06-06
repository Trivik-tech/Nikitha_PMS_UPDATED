package com.triviktech.controllers.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employeeslist.EmployeesList;
import com.triviktech.payloads.response.global.Response;

import com.triviktech.payloads.response.hr.HrResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    ResponseEntity<EmployeeInfo> updateEmployee(@PathVariable String empId, @RequestBody Employee employee);

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
    ResponseEntity<Map<String,String>> registerEmployee(@RequestBody Employee employee);

    @GetMapping("/pending")
    ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR();

    @GetMapping("/percentage")
    ResponseEntity<PmsPercentageDto> getPmsPercentageForHR();
}
