package com.triviktech.controllers.employee;

import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.services.employee.EmployeeService;
import com.triviktech.services.krakpi.KraKpiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeControllerImpl implements EmployeeController{

    private final EmployeeService employeeService;
    private final KraKpiService kraKpiService;

    public EmployeeControllerImpl(EmployeeService employeeService, KraKpiService kraKpiService) {
        this.employeeService = employeeService;
        this.kraKpiService = kraKpiService;
    }

    @Override
    public ResponseEntity<?> registerEmployee(EmployeeInformationRequestDto employeeInformationRequestDto) {
        return new ResponseEntity<>(employeeService.registerEmployee(employeeInformationRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeInformationResponseDto>> listOfEmployees(int pageSize, int pageNumber) {

        return ResponseEntity.ok(employeeService.listOfEmployees(pageSize, pageNumber));
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(String employeeId) {
        return ResponseEntity.ok(kraKpiService.kraKpiForEmployee(employeeId));
    }

    @Override
    public ResponseEntity<KraKpiResponseDto> kraKpiRegistrationForEmployee(KraKpiRequestDto kraKpiRequestDto) {
        return new ResponseEntity<>(kraKpiService.registerKraKpi(kraKpiRequestDto),HttpStatus.CREATED);
    }
}
