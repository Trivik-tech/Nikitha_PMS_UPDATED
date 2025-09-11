package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employee.PmsStatuscountDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.krakpi.KraKpiRepository;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.manager.ManagerService;
import com.triviktech.services.notification.NotificationService;
import com.triviktech.utilities.validation.ValidationMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ManagerControllerImpl implements ManagerController {
    @Autowired
    private EmployeeInformationRepository employeeRepository;
    @Autowired
    private KraKpiRepository kraKpiRepository;

    @Autowired
    private NotificationService notificationService;

    private final ManagerService managerService;
    private final KraKpiService kraKpiService;

    public ManagerControllerImpl(ManagerService managerService, KraKpiService kraKpiService) {
        this.managerService = managerService;
        this.kraKpiService = kraKpiService;
    }

    @Override
    public ResponseEntity<?> registerManager(ManagerRequestDto managerRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationMessage validationMessage = new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(managerService.registerManager(managerRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ManagerResponseDto>> listOfManagers() {
        return ResponseEntity.ok(managerService.listOfManager());
    }

    @Override
    public ResponseEntity<ManagerResponseDto> getManager(String managerId) {
        ManagerResponseDto managerById = managerService.findManagerById(managerId);
        return ResponseEntity.ok(managerById);
    }

    @Override
    public ResponseEntity<ManagerResponseDto> profile(@AuthenticationPrincipal UserDetails manager) {
        System.out.println(manager.getUsername());
        return ResponseEntity.ok(managerService.findManagerById(manager.getUsername()));
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> listOfEmployeesForManager(String managerId) {
        return ResponseEntity.ok(managerService.listOfEmployeesForManager(managerId));
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSPendingEmployees(String managerId) {
        return ResponseEntity.ok(managerService.listOfPMSPendingEmployees(managerId));
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> listOfPMSCompletedEmployees(String managerId) {
        return ResponseEntity.ok(managerService.listOfPMSCompletedEmployees(managerId));
    }

    @Override
    public ResponseEntity<Map<String, String>> managerReview(String managerId, String employeeId,
                                                             KraKpiRequestDto data) {

        Map<String, String> response = managerService.managerReview(managerId, employeeId, data);

        if ("success".equalsIgnoreCase(response.get("status"))) {

            // Step 1: Get employee name
            Optional<String> employeeNameOpt = employeeRepository.findNameByEmpId(employeeId);

            if (employeeNameOpt.isPresent()) {
                String employeeName = employeeNameOpt.get();

                // Step 2: Notify employee
                String empDestination = "/queue/employee-notification";
                String empContent = "Manager Review has been completed for " + employeeName + " (" + employeeId + ")";
                notificationService.sendMessageWithRecent("System", employeeId, empContent, empDestination);

                // Step 3: Notify HR (if available)
                Optional<String> optionalHrId = employeeRepository.findHrIdByEmployeeId(employeeId);
                optionalHrId.ifPresent(hrId -> {
                    String hrDestination = "/queue/hr-notification";
                    String hrContent = "Manager Review completed for " + employeeName + " (" + employeeId + ")";
                    notificationService.sendMessageWithRecent("System", hrId, hrContent, hrDestination);
                });

            } else {
                System.out.printf("Employee name not found for employee ID: ", employeeId);
            }
        }

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<KraKpiResponseDto> getKraKpis(String managerId, String employeeId) {
        return ResponseEntity.ok(managerService.getEmployeeKarKpi(managerId, employeeId));
    }

    @Override
    public ResponseEntity<Map<String, String>> approveKraKpi(String employeeId, String managerId,
                                                             KraKpiRequestDto kraKpiRequestDto) {

        Map<String, String> response = managerService.approveKra(kraKpiRequestDto, employeeId, managerId);

        if ("Approved".equalsIgnoreCase(response.get("status"))) {

            // Fetch employee name
            Optional<String> employeeNameOpt = employeeRepository.findNameByEmpId(employeeId);

            if (employeeNameOpt.isPresent()) {
                String employeeName = employeeNameOpt.get();

                // Notify employee
                String empDestination = "/queue/employee-notification";
                String empContent = "KRA/KPI approved for " + employeeName + " (" + employeeId + ")";
                notificationService.sendMessageWithRecent("System", employeeId, empContent, empDestination);

                // Get employee → assigned HR
                Optional<String> optionalHrId = employeeRepository.findHrIdByEmployeeId(employeeId);
                System.out.println("Assigned HR ID: " + optionalHrId);

                if (optionalHrId.isPresent()) {
                    String hrId = optionalHrId.get();
                    String hrDestination = "/queue/hr-notification";
                    String hrContent = "KRA/KPI approved for " + employeeName + " (" + employeeId + ")";
                    notificationService.sendMessageWithRecent("System", hrId, hrContent, hrDestination);
                } else {
                    System.out.printf("No HR assigned to employee ID: ", employeeId);
                }

            } else {
                System.out.printf("Employee name not found for employee ID: ", employeeId);
            }
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getCompletedPmsForManager(String reportingManager) {
        return ResponseEntity.ok(managerService.getCompletedAssessmentListForManager(reportingManager));
    }

    @Override
    public ResponseEntity<List<EmployeeWithPmsStatus>> getPendingPmsForManager(String reportingManager) {
        return ResponseEntity.ok(managerService.getPendingAssessmentListForManager(reportingManager));
    }

    @Override
    public ResponseEntity<PmsPercentageDto> getPmsPercentageForManager(String reportingManager) {
        return ResponseEntity.ok(managerService.getPmsPercentageForManager(reportingManager));
    }

    @Override
    public ResponseEntity<Map<String, String>> notifyEmployee(String employeeId) {
        // 1. Check if employee has completed self-review
        boolean selfCompleted = kraKpiRepository.findSelfCompletedStatusByEmployeeId(employeeId).orElse(false);

        // 2. If not completed, send notification
        if (!selfCompleted) {
            String destination = "/queue/employee-notification";
            String content = "Reminder: Please complete your PMS self-assessment.";

            // Send message and persist
            notificationService.sendMessageWithRecent("System", employeeId, content, destination);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Notification sent successfully to the employee"));
        }

        // 3. If already completed, don't send
        return ResponseEntity.ok(Map.of(
                "status", "skipped",
                "message", "No notification sent. Self-assessment already completed."));
    }

    @Override
    public ResponseEntity<Map<String, Integer>> getTeamSize(String managerId) {
        return ResponseEntity.ok(managerService.getTimeSize(managerId));
    }

   @Override
public ResponseEntity<PmsStatuscountDto> getPmsCountsForManager(String managerId) {
    PmsStatuscountDto dto = managerService.getPmsCountsForManager(managerId);
    return ResponseEntity.ok(dto);
}

// @Override
// public void autoNotifyEmployees() {
//     // Get all employees
//     List<String> allEmployeeIds = employeeRepository.findAllEmployeeIds();

//     for (String employeeId : allEmployeeIds) {
//         try {
//             System.out.println("Auto Notify check for employeeId: " + employeeId);

//             // 1. Check if PMS initiated for this employee
//             Boolean pmsInitiated = kraKpiRepository
//                     .findPmsInitiatedByEmployeeId(employeeId)
//                     .orElse(false);

//             if (!pmsInitiated) {
//                 System.out.println("PMS not initiated for " + employeeId + ", skipping...");
//                 continue;
//             }

//             // 2. Check if self-review is completed
//             boolean selfCompleted = kraKpiRepository
//                     .findSelfCompletedStatusByEmployeeId(employeeId)
//                     .orElse(false);

//             if (!selfCompleted) {
//                 // 3. Send reminder to employee
//                 String destination = "/queue/employee-notification";
//                 String content = "Reminder: Please complete your PMS self-assessment.";

//                 notificationService.sendMessageWithRecent("System", employeeId, content, destination);

//                 System.out.println("Notification sent to employee " + employeeId);
//             } else {
//                 System.out.println("Self-assessment already completed for " + employeeId + ", skipping...");
//             }

//         } catch (Exception ex) {
//             System.out.println("ERROR while notifying " + employeeId + ": " + ex.getMessage());
//             ex.printStackTrace();
//         }
//     }

// }
}
