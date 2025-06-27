package com.triviktech.controllers.employee;

import com.triviktech.entities.notification.Notification;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.repositories.notification.NotificationRepository;
import com.triviktech.services.employee.EmployeeService;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.notification.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService employeeService;
    private final KraKpiService kraKpiService;
    private final SimpMessagingTemplate messagingTemplate;

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
        String reportingManager = "EMP1234";
        String destination = "/queue/manager-notification";
        String content = "KraKpi Registered for employee ID: " + kraKpiRequestDto.getEmployeeId();
        // String reportingManager = "EMP001";
        // String destination = "/queue/hr-notification";
        // String content = "KraKpi Registered for employee ID: " + kraKpiRequestDto.getEmployeeId();

        notificationService.sendMessageWithRecent("System", reportingManager, content, destination);

        Map<String, String> response = kraKpiService.registerKraKpi(kraKpiRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    

    @Override
    public ResponseEntity<EmployeeInfo> profile(String employeeId) {
        return ResponseEntity.ok(employeeService.profile(employeeId));
    }

    @Override
    public ResponseEntity<Map<String, String>> selfReview(KraKpiRequestDto kraKpiRequestDto, String employeeId) {
        return ResponseEntity.ok(kraKpiService.employeeReview(kraKpiRequestDto, employeeId));
    }
}
