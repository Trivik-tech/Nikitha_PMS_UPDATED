package com.triviktech.controllers.employee;

import com.triviktech.entities.notification.Notification;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.repositories.employee.EmployeeInformationRepository;
import com.triviktech.repositories.notification.NotificationRepository;
import com.triviktech.services.employee.EmployeeService;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService employeeService;
    private final KraKpiService kraKpiService;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmployeeInformationRepository employeeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private NotificationRepository notificationRepository;

    public EmployeeControllerImpl(EmployeeService employeeService, KraKpiService kraKpiService,
            SimpMessagingTemplate messagingTemplate) {
        this.employeeService = employeeService;
        this.kraKpiService = kraKpiService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(String employeeId) {
        return ResponseEntity.ok(kraKpiService.kraKpiForEmployee(employeeId));
    }

    @Override
    public ResponseEntity<Map<String, String>> kraKpiRegistrationForEmployee(KraKpiRequestDto kraKpiRequestDto) {
        // Step 1: Call service to register KRA/KPI
        Map<String, String> response = kraKpiService.registerKraKpi(kraKpiRequestDto);

        // Step 2: Check if registration was successful
        if ("Success".equalsIgnoreCase(response.get("Status"))) {
            String employeeId = kraKpiRequestDto.getEmployeeId();

            // Step 3: Fetch reporting manager ID
            String reportingManagerId = employeeRepository.findReportingManagerIdByEmployeeId(employeeId);
            System.out.println("Reporting Manager ID: " + reportingManagerId);

            // Step 4: Proceed only if manager ID is found
            if (reportingManagerId != null && !reportingManagerId.isEmpty()) {

                // Step 5: Fetch employee name and send notification if present
                Optional<String> employeeNameOpt = employeeRepository.findNameByEmpId(employeeId);

                if (employeeNameOpt.isPresent()) {
                    String employeeName = employeeNameOpt.get();
                    String content = "KRA/KPI registered for " + employeeName + " (" + employeeId + ")";
                    String destination = "/queue/manager-notification";

                    // Send WebSocket notification to the manager
                    notificationService.sendMessageWithRecent("System", reportingManagerId, content, destination);
                } else {
                    System.out.printf("Employee name not found for employee ID: ", employeeId);
                }

            } else {
                System.out.printf("No reporting manager found for employee ID: ", employeeId);
            }
        } else {
            System.out.printf("KRA/KPI registration failed for employee ID: ", kraKpiRequestDto.getEmployeeId());
        }

        // Step 6: Return the service response to the frontend
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<EmployeeInfo> profile(UserDetails employee) {
        return ResponseEntity.ok(employeeService.profile(employee.getUsername()));
    }

    @Override
    public ResponseEntity<Map<String, String>> selfReview(KraKpiRequestDto kraKpiRequestDto, String employeeId) {
        Map<String, String> response = kraKpiService.employeeReview(kraKpiRequestDto, employeeId);

        if ("success".equalsIgnoreCase(response.get("status"))) {
            // Get reporting manager ID
            String reportingManagerId = employeeRepository.findReportingManagerIdByEmployeeId(employeeId);

            if (reportingManagerId != null && !reportingManagerId.isEmpty()) {

                // Fetch employee name
                Optional<String> employeeNameOpt = employeeRepository.findNameByEmpId(employeeId);

                if (employeeNameOpt.isPresent()) {
                    String employeeName = employeeNameOpt.get();

                    // Compose personalized notification
                    String destination = "/queue/manager-notification";
                    String content = employeeName + " (" + employeeId + ") has submitted self review.";

                    // Send notification to manager
                    notificationService.sendMessageWithRecent("System", reportingManagerId, content, destination);
                } else {
                    System.out.printf("Employee name not found for employee ID: ", employeeId);
                }

            } else {
                System.out.printf("No reporting manager found for employee ID: ", employeeId);
            }
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> checkKraKpiAlreadyExists(String employeeId) {
        return ResponseEntity.ok(kraKpiService.existsByEmployee(employeeId));
    }
}
