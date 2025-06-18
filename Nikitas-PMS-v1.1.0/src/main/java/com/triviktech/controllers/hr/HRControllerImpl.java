package com.triviktech.controllers.hr;

import com.triviktech.exception.validation.ValidationException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.services.hr.HrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HRControllerImpl implements HRController {

    private final HrService hrService;

    public HRControllerImpl(HrService hrService) {
        this.hrService = hrService;
    }

    @Override
    public ResponseEntity<?> registerHr(HrRequestDto hrRequestDto) {
        return new ResponseEntity<>(hrService.registerHr(hrRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HrResponseDto> getHrById(String hrId) {
        return ResponseEntity.ok(hrService.findHrById(hrId));
    }

    @Override
    public ResponseEntity<List<Object>> uploadEmployeesData(MultipartFile file) throws IOException {
        return new ResponseEntity<>(hrService.uploadEmployeesData(file), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> allEmployees() {
        return ResponseEntity.ok(hrService.getAllEmployees());
    }

    @Override
    public ResponseEntity<EmployeeInfo> getEmployee(String employeeId) {
        return ResponseEntity.ok(hrService.getEmployeeById(employeeId));
    }

    @Override
    public ResponseEntity<Map<String, Integer>> totalEmployees() {
        Map<String, Integer> response = new HashMap<>();
        response.put("totalEmployees", hrService.totalEmployees());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> searchEmployee(String search) {
        return ResponseEntity.ok(hrService.searchEmployee(search));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteEmployee(String employeeId) {
        Map<String, String> msg = hrService.deleteEmployee(employeeId);
        return ResponseEntity.ok(msg);
    }

    @Override
    public ResponseEntity<EmployeeInfo> updateEmployee(String empId, Employee employee) {
        EmployeeInfo employeeInfo = hrService.updateEmployee(empId, employee);
        return ResponseEntity.ok(employeeInfo);
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> employeeListWithKraKpiApproved() {
        return ResponseEntity.ok(hrService.employeesWithKraKpiApproval());
    }

    @Override
    public ResponseEntity<Map<String, Object>> getDepartment() {
        Map<String, Object> getdepartment = hrService.getdepartment();
        return ResponseEntity.ok(getdepartment);
    }

    @Override
    public ResponseEntity<Map<String, List<Long>>> employeeCount() {
        List<Long> employeeCounts = hrService.getEmployeeCountByDepartment();
        Map<String, List<Long>> response = Map.of("employees", employeeCounts);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Integer>> keyMatrix() {
        Map<String, Integer> employeeKeyMatrix = hrService.assessmentKeyMatrix();
        return ResponseEntity.ok(employeeKeyMatrix);
    }

    @Override
    public ResponseEntity<Map<String, String>> pmsInitiated(String employeeId, Map<String, Boolean> pms) {
        return ResponseEntity.ok(hrService.initiatePms(employeeId, pms));
    }

    @Override
    public ResponseEntity<List<EmployeeInfo>> pmsInitiatedEmployees() {
        return ResponseEntity.ok(hrService.pmsInitiatedEmployees());
    }

    @Override
    public ResponseEntity<Map<String, String>> registerEmployee(Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(hrService.employeeRegistration(employee), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForHR() {
        return ResponseEntity.ok(hrService.getCompletedPmsForHR());
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForHR() {
        return ResponseEntity.ok(hrService.getPendingPmsForHR());
    }

    @Override
    public ResponseEntity<PmsPercentageDto> getPmsPercentageForHR() {
        return ResponseEntity.ok(hrService.getPmsPercentageForHR());
    }
}
