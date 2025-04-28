package com.triviktech.controllers.employee;

import com.triviktech.constants.AppConstants;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1/pms/employee")
@CrossOrigin(origins = "http://localhost:3000")
public interface EmployeeController {

    @PostMapping("/register-employee")
    ResponseEntity<?> registerEmployee(@RequestBody EmployeeInformationRequestDto employeeInformationRequestDto);

    @GetMapping("/employee-list")
    ResponseEntity<List<EmployeeInformationResponseDto>>
    listOfEmployees(@RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                    @RequestParam(name = "pageNumber",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNumber);

    @GetMapping("/kra-kpi-list/{employeeId}")
    ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(@PathVariable String employeeId);

    @PostMapping("/register-kra-kpi")
    ResponseEntity<KraKpiResponseDto> kraKpiRegistrationForEmployee(@RequestBody KraKpiRequestDto kraKpiRequestDto);


}
