package com.triviktech.services.hr;

import com.triviktech.payloads.request.employee.Employee;
import com.triviktech.payloads.request.hr.HrRequestDto;
import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.payloads.response.employee.EmployeeWithPmsStatus;
import com.triviktech.payloads.response.employee.PmsPercentageDto;
import com.triviktech.payloads.response.employeeslist.EmployeesList;
import com.triviktech.payloads.response.global.Response;

import com.triviktech.payloads.response.hr.HrResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HrService {

    HrResponseDto registerHr(HrRequestDto hrRequestDto);

    HrResponseDto findHrById(String hrId);

    List<Object> uploadEmployeesData(MultipartFile file) throws IOException;

    List<EmployeeInfo> getAllEmployees();

    EmployeeInfo getEmployeeById(String employeeId);

    Integer totalEmployees();

    List<EmployeeInfo> searchEmployee(String search);

    Map<String, String> deleteEmployee(String employeeId);

    EmployeeInfo updateEmployee(String empId, Employee employee);

    Map<String, Object> getdepartment();

    List<Long> getEmployeeCountByDepartment();

    Map<String, Integer> assessmentKeyMatrix();

    List<EmployeeInfo> employeesWithKraKpiApproval();

    Map<String, String> initiatePms(String employeeId, Map<String, Boolean> pms);

    List<EmployeeInfo> pmsInitiatedEmployees();

    Map<String, String> employeeRegistration(Employee employee);

    List<EmployeeWithPmsStatus> getCompletedPmsForHR();

    List<EmployeeWithPmsStatus> getPendingPmsForHR();

    PmsPercentageDto getPmsPercentageForHR();


}
