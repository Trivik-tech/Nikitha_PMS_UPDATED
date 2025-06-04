package com.triviktech.services.employee;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.employee.EmployeeInformationRequestDto;
import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeInformationResponseDto;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    EmployeeInformationResponseDto registerEmployee(EmployeeInformationRequestDto employeeInformationRequestDto);
    List<EmployeeInformationResponseDto> listOfEmployees(int pageSize,int pageNumber);

    EmployeeInfo profile(String employeeId);



    }





