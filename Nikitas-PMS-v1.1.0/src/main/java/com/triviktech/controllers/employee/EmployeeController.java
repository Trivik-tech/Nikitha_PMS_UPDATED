package com.triviktech.controllers.employee;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/api/v1/pms/employee")
@CrossOrigin(origins = "http://localhost:3000")
public interface EmployeeController {

    @GetMapping("/kra-kpi-list/{employeeId}")
    ResponseEntity<KraKpiResponseDto> kraKpiForEmployee(@PathVariable String employeeId);

    @PostMapping("/register-kra-kpi")
    ResponseEntity<Map<String,String>> kraKpiRegistrationForEmployee(@RequestBody KraKpiRequestDto kraKpiRequestDto);

    @GetMapping("/profile/{employeeId}")
    ResponseEntity<EmployeeInfo> profile(@PathVariable String employeeId);

    @PutMapping("/self-review/{employeeId}")
    ResponseEntity<Map<String,String >> selfReview(@RequestBody KraKpiRequestDto kraKpiRequestDto,@PathVariable String employeeId);

}
