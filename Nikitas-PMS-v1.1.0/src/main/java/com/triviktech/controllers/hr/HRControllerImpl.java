package com.triviktech.controllers.hr;

import com.triviktech.exception.validation.ValidationException;
import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.hr.HrResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.services.hr.HrService;
import com.triviktech.services.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HRControllerImpl implements HRController {
    @Autowired
    NotificationService notificationService;
    @Autowired
    private KraKpiRepository kraKpiRepository;

    @Autowired
    private EmployeeInformationRepository employeeRepository;

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
    public ResponseEntity<Map<String, String>> updateEmployee(String empId, Employee employee) {
        Map<String, String> response = hrService.updateEmployee(empId, employee);
        return ResponseEntity.ok(response);
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
        Map<String, String> response = hrService.initiatePms(employeeId, pms);

        // Only send message if PMS initiation is successful
        if ("success".equalsIgnoreCase(response.get("status"))) {
            String destination = "/queue/employee-notification";
            String content = "PMS has been initiated for you. Please complete your KRA/KPI self-review.";
            notificationService.sendMessageWithRecent("System", employeeId, content, destination);
        }

        return ResponseEntity.ok(response);
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
@Override
public ResponseEntity<Map<String, String>> notifyEmployeeAndManager(String employeeId) {
    try {
        System.out.println("🔔 Notify called for employeeId: " + employeeId);

        boolean selfCompleted = kraKpiRepository.findSelfCompletedStatusByEmployeeId(employeeId).orElse(false);
        System.out.println("✅ Self completed: " + selfCompleted);

        String managerId = employeeRepository.findReportingManagerIdByEmployeeId(employeeId);
        System.out.println("👤 Manager ID: " + managerId);

        String employeeName = employeeRepository.findNameByEmpId(employeeId).orElse("Unknown");

        if (managerId == null || managerId.isBlank()) {
            System.out.println("❌ Manager not found for " + employeeId);
            return ResponseEntity.status(400).body(Map.of(
                "status", "error",
                "message", "Manager not found for this employee."
            ));
        }

        String empDestination = "/queue/employee-notification";
        String managerDestination = "/queue/manager-notification";

        try {
            if (selfCompleted) {
                // Only manager gets notified with a different message
                String managerContent = "Reminder: " + employeeName + " has completed self-review. Please complete your Manager review.";
                notificationService.sendMessageWithRecent("System", managerId, managerContent, managerDestination);
            } else {
                // Both get notified with appropriate messages
                String empContent = "Reminder: Please complete your PMS self-review.";
                String managerContent = "Reminder: " + employeeName + " has not yet completed self-review. Please follow up.";

                notificationService.sendMessageWithRecent("System", employeeId, empContent, empDestination);
                notificationService.sendMessageWithRecent("System", managerId, managerContent, managerDestination);
            }

        } catch (Exception ex) {
            System.out.println("❌ ERROR inside sendMessageWithRecent: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "Notification failed: " + ex.getMessage()
            ));
        }

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Notification sent based on PMS status."
        ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of(
            "status", "error",
            "message", "Notification failed: " + e.getMessage()
        ));
    }
}

    @Override
    public ResponseEntity<Map<String, HrResponseDto>> profile(UserDetails hr) {
        Map<String,HrResponseDto> response=new HashMap<>();
        HrResponseDto profile = hrService.profile(hr.getUsername());
        if(profile!=null){
            response.put("profile",profile);
            return ResponseEntity.ok(response);
        }
        response.put("message",null);
        return ResponseEntity.ok(response);
    }
}
