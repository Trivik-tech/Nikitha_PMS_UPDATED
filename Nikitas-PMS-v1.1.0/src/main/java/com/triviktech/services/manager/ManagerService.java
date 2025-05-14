package com.triviktech.services.manager;

import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;

import java.util.List;
import java.util.Map;

public interface ManagerService {


    ManagerResponseDto registerManager(ManagerRequestDto managerRequestDto);

    List<ManagerResponseDto> listOfManager();

    ManagerResponseDto findManagerById(String managerId);

    List<EmployeeWithPmsStatus> listOfEmployeesForManager(String managerId);

    List<EmployeeWithPmsStatus> listOfPMSCompletedEmployees(String managerId);
    List<EmployeeWithPmsStatus> listOfPMSPendingEmployees(String managerId);

    KraKpiResponseDto getEmployeeKarKpi(String managerName, String employeeId);

    Map <String, String> approveKra (Map<String, Boolean> approve, String employeeId, String reportingManager);


}