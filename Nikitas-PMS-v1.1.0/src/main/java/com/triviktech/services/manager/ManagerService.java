package com.triviktech.services.manager;

import com.triviktech.payloads.request.krakpi.KraKpiRequestDto;
import com.triviktech.payloads.request.manager.ManagerRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;
import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.krakpi.KraKpiResponseDto;
import com.triviktech.payloads.response.manager.ManagerResponseDto;

import java.util.List;
import java.util.Map;

public interface ManagerService {

    ManagerResponseDto registerManager(ManagerRequestDto managerRequestDto);

    List<ManagerResponseDto> listOfManager();

    ManagerResponseDto findManagerById(String managerId);

    List<EmployeeWithPmsStatus> listOfEmployeesForManager(String reportingManager);

//    List<EmployeeInfo> findAllByReportingManager(String name);

    List<EmployeeWithPmsStatus> listOfPMSCompletedEmployees(String managerId);

    List<EmployeeWithPmsStatus> listOfPMSPendingEmployees(String managerId);

    KraKpiResponseDto getEmployeeKarKpi(String managerName, String employeeId);

    Map<String, String> approveKra(KraKpiRequestDto kraKpiRequestDto, String employeeId, String managerId);

    Map<String, String> managerReview(String managerId, String employeeId, KraKpiRequestDto data);

    List<EmployeeWithPmsStatus> getPendingAssessmentListForManager(String reportingManager);

    List<EmployeeWithPmsStatus> getCompletedAssessmentListForManager(String reportingManager);

    PmsPercentageDto getPmsPercentageForManager(String reportingManager);

    Map<String,Integer> getTimeSize(String managerId);
}