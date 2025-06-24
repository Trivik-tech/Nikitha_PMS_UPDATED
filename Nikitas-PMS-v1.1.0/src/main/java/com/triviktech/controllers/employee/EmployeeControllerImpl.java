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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService employeeService;
    private final KraKpiService kraKpiService;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    NotificationService notificationService;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    NotificationRepository notificationRepository;

    public EmployeeControllerImpl(EmployeeService employeeService, KraKpiService kraKpiService,
            SimpMessagingTemplate messagingTemplate) {
        this.employeeService = employeeService;
        this.kraKpiService = kraKpiService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public ResponseEntity<?> registerEmployee(EmployeeInformationRequestDto employeeInformationRequestDto) {
        return new ResponseEntity<>(employeeService.registerEmployee(employeeInformationRequestDto),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeInformationResponseDto>> listOfEmployees(int pageSize, int pageNumber) {

        return ResponseEntity.ok(employeeService.listOfEmployees(pageSize, pageNumber));
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(String employeeId) {
        return ResponseEntity.ok(kraKpiService.kraKpiForEmployee(employeeId));
    }

//     @Override
//     public ResponseEntity<KraKpiResponseDto> kraKpiRegistrationForEmployee(KraKpiRequestDto kraKpiRequestDto) {
//         // EmployeeInfo employee = employeeService.profile(kraKpiRequestDto.getEmployeeId());
//         String reportingManager ="NB238";
//         String destination ="/queue/manager-notification";
//         String content="KraKpiRegistred for employee with id :";
//         // notificationService.sendMessage("System",reportingManager,content,destination);
//        boolean isOnline = false;
// for (int i = 0; i < 3; i++) {
//     isOnline = simpUserRegistry.getUser(reportingManager) != null;
   
//     if (isOnline) break;
//      System.out.println("is online");
//     try {
//         Thread.sleep(1000); // wait 100ms before retrying
//     } catch (InterruptedException e) {
//         Thread.currentThread().interrupt();
//     }
// }

//          if (isOnline) {
//             messagingTemplate.convertAndSendToUser(reportingManager, destination, content);
//             System.out.println("sent ok");
//             notificationService.saveMessage("System",reportingManager,content,destination,true);
//          }else {
//         notificationService.saveMessage("System", reportingManager, content,destination,false);
//     }
        
//         KraKpiResponseDto kraKpiResponseDto = kraKpiService.registerKraKpi(kraKpiRequestDto);
//         return new ResponseEntity<>(kraKpiResponseDto, HttpStatus.CREATED);

//     }
//     @GetMapping("/test-notification")
// public ResponseEntity<String> sendTestNotification() {
//     messagingTemplate.convertAndSendToUser("NB238", "/queue/manager-notification", "🔔 Test WebSocket message");
//     return ResponseEntity.ok("Notification sent");
// }
@GetMapping("/recent")
    public ResponseEntity<List<Notification>> getRecentMessages() {
        

        // String username = manager.getUsername(); //  This will be injected from SecurityContext
        // System.out.println("Authenticated username: " + username);
String username = "EMP1234"; // This will be injected from SecurityContext
        System.out.println(" Authenticated username: " + username);
        List<Notification> undelivered = notificationService.getPendingNotification(username);
        int remaining = 50 - undelivered.size();

        List<Notification> recent = notificationRepository
                .findTop50ByReceiverAndDeliveredTrueOrderByTimestampDesc(username)
                .stream()
                .limit(Math.max(remaining, 0))
                .toList();

        List<Notification> all = new ArrayList<>();
        all.addAll(undelivered);
        all.addAll(recent);

        return ResponseEntity.ok(all);
    }

@Override
public ResponseEntity<KraKpiResponseDto> kraKpiRegistrationForEmployee(KraKpiRequestDto kraKpiRequestDto) {
    String reportingManager = "EMP1234";
    String destination = "/queue/manager-notification";
    String content = "KraKpi Registered for employee ID: " + kraKpiRequestDto.getEmployeeId();

    notificationService.sendMessageWithRecent("System", reportingManager, content, destination);

    KraKpiResponseDto kraKpiResponseDto = kraKpiService.registerKraKpi(kraKpiRequestDto);
    return new ResponseEntity<>(kraKpiResponseDto, HttpStatus.CREATED);
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
