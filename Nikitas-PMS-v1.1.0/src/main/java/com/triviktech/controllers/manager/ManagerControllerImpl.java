package com.triviktech.controllers.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;
import com.triviktech.services.krakpi.KraKpiService;
import com.triviktech.services.manager.ManagerService;
import com.triviktech.utilities.validation.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ManagerControllerImpl implements ManagerController {

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
    public ResponseEntity<Map<String,String>> managerReview(String managerId,String employeeId, KraKpiRequestDto data) {
        return ResponseEntity.ok(managerService.managerReview(managerId,employeeId,data));
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> getKraKpis(String managerId, String employeeId) {
        return ResponseEntity.ok(managerService.getEmployeeKarKpi(managerId, employeeId));
    }

//    @Override
//    public ResponseEntity<List<EmployeeInfo>> getManagerTeam(String reportingManager) {
//        return ResponseEntity.ok(managerService.findAllByReportingManager(reportingManager));
//    }

    @Override
    public ResponseEntity<Map<String, String>> approveKraKpi(String employeeId, String managerId, KraKpiRequestDto kraKpiRequestDto) {
        return ResponseEntity.ok(managerService.approveKra(kraKpiRequestDto, employeeId, managerId));
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
}